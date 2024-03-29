import javax.swing.JFrame;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

class Animação
{
    // this interface lets you create different animations without having to worry about anything but drawing
    public interface Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber);
    }

    // returns the current time as a double
    public static double now() {
        long t = System.currentTimeMillis();

        return (double)t / 1000.0f;
    }

    public static class AnimationComponent extends Component {
        Animation animation;
        // keeps track of the time and frame
        int frame = 0;
        double startTime = now();
        double lastTime = now();

        // use this timer to run the animation
        Timer timer = new Timer();

        AnimationComponent(Animation animation) {
            this.animation = animation;

            // start the timer.
            // Its bets to do this only when the component is shown, but I don't
            // remember how to do this.  For now just start the animation after one second.
            // The 1000 / 30 will be 30 frames per second.
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        public void run() {
                            repaint();
                        }
                    }
                    , 1000, 1000 / 30);
        }

        // override the component paint method to update the time and call the animation
        public void paint(Graphics g) {
            double nowTime = now();

            frame += 1;

            g.clearRect(0, 0, getWidth(), getHeight());
            animation.drawFrame(g, getSize(), nowTime - startTime, nowTime - lastTime, frame);

            lastTime = nowTime;
        }

        // return the size we want the window to be 640x480
        public Dimension getPreferredSize() {
            return new Dimension(640, 480);
        }
    }

    // test animation to draw a sine wave
    public static class SineWave implements Animation {
        public void drawFrame(Graphics g, Dimension size, double frameTime, double deltaTime, int frameNumber) {
            // set the point in the window halfway down for the "zero" point.
            double zero = size.getHeight() / 2.0;

            // 100 amplitude, 2hz
            double y = zero + 100 * Math.sin(2 * frameTime);

            // scale x to make the window 5 seconds wide
            double tx = frameTime / 5 - (int)(frameTime / 5);
            double x = tx * size.getWidth();

            // draw a line and a circle on the sine wave
            g.drawLine(0, (int)zero, (int)size.getWidth(), (int)zero);
            g.drawOval((int)x, (int)y, 10, 10);
        }
    }

    public static void main(String[] args) {
        // create a frame and add an animation component to it.
        JFrame frame = new JFrame("Animation");

        frame.add(new AnimationComponent(new SineWave()));

        frame.pack();

        frame.show();

    }
}