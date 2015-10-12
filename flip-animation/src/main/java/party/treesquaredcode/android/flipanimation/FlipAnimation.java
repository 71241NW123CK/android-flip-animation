package party.treesquaredcode.android.flipanimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

/**
 * A utility for flipping from one {@link View} to another.  The outbound view has its visibility
 * set to {@link View#GONE} at the end of the animtion.  A listener can be attached which will be
 * notified when the animation starts and ends, or fails for some reason.
 * Created by rht on 10/12/15.
 */
public class FlipAnimation {
    private static final String TAG = FlipAnimation.class.getCanonicalName();
    private static final long DEFAULT_DURATION_MILLIS = 500L;

    private View outView;
    private View inView;
    private AnimationListener animationListener;
    private Animation outAnimation;
    private Animation inAnimation;

    public static Builder animation() {
        return new Builder();
    }

    private FlipAnimation() {

    }

    private FlipAnimation(@NonNull View outView, @NonNull View inView, long durationMillis, @Nullable Interpolator interpolator, @NonNull Direction direction, @Nullable AnimationListener animationListener) {
        if (durationMillis <= 0L) {
            Log.d(TAG, "Cannot use nonpositive duration.  Setting to default of " + DEFAULT_DURATION_MILLIS + "ms.");
            durationMillis = DEFAULT_DURATION_MILLIS;
        }
        this.outView = outView;
        this.inView = inView;
        this.animationListener = animationListener;
        this.outAnimation = direction == Direction.LEFT ? new FlipOutLeftAnimation() : new FlipOutRightAnimation();
        this.inAnimation = direction == Direction.LEFT ? new FlipInLeftAnimation() : new FlipInRightAnimation();
        this.outAnimation.setDuration(durationMillis);
        this.inAnimation.setDuration(durationMillis);
        if (interpolator != null) {
            this.outAnimation.setInterpolator(interpolator);
            this.inAnimation.setInterpolator(interpolator);
        }
    }

    private void run() {
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationStart(FlipAnimation.this);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationEnd(FlipAnimation.this);
                }
                outView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        inView.setVisibility(View.VISIBLE);
        inView.startAnimation(inAnimation);
        outView.startAnimation(outAnimation);
    }

    public enum Direction {
        LEFT,
        RIGHT
    }

    private static class FlipOutLeftAnimation extends Animation {
        float halfWidth;
        float halfHeight;
        Camera camera = new Camera();

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            halfWidth = 0.5f * width;
            halfHeight = 0.5f * height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix tMatrix = t.getMatrix();
            camera.save();
            camera.translate(0, 0, (float) (halfWidth * Math.sin(Math.PI * interpolatedTime)));
            camera.rotateY(-180 * interpolatedTime);
            camera.getMatrix(tMatrix);
            camera.restore();
            tMatrix.preTranslate(-halfWidth, -halfHeight);
            tMatrix.postTranslate(halfWidth, halfHeight);
            t.setAlpha(interpolatedTime < 0.5f ? 1.0f : 0.0f);
        }
    }

    private static class FlipInLeftAnimation extends Animation {
        float halfWidth;
        float halfHeight;
        Camera camera = new Camera();

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            halfWidth = 0.5f * width;
            halfHeight = 0.5f * height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix tMatrix = t.getMatrix();
            camera.save();
            camera.translate(0, 0, (float) (halfWidth * Math.sin(Math.PI * interpolatedTime)));
            camera.rotateY(180 * (1.0f - interpolatedTime));
            camera.getMatrix(tMatrix);
            camera.restore();
            tMatrix.preTranslate(-halfWidth, -halfHeight);
            tMatrix.postTranslate(halfWidth, halfHeight);
            t.setAlpha(interpolatedTime < 0.5f ? 0.0f : 1.0f);
        }
    }

    private static class FlipOutRightAnimation extends Animation {
        float halfWidth;
        float halfHeight;
        Camera camera = new Camera();

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            halfWidth = 0.5f * width;
            halfHeight = 0.5f * height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix tMatrix = t.getMatrix();
            camera.save();
            camera.translate(0, 0, (float) (halfWidth * Math.sin(Math.PI * interpolatedTime)));
            camera.rotateY(180 * interpolatedTime);
            camera.getMatrix(tMatrix);
            camera.restore();
            tMatrix.preTranslate(-halfWidth, -halfHeight);
            tMatrix.postTranslate(halfWidth, halfHeight);
            t.setAlpha(interpolatedTime < 0.5f ? 1.0f : 0.0f);
        }
    }

    private static class FlipInRightAnimation extends Animation {
        float halfWidth;
        float halfHeight;
        Camera camera = new Camera();

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            halfWidth = 0.5f * width;
            halfHeight = 0.5f * height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final Matrix tMatrix = t.getMatrix();
            camera.save();
            camera.translate(0, 0, (float) (halfWidth * Math.sin(Math.PI * interpolatedTime)));
            camera.rotateY(-180 * (1.0f - interpolatedTime));
            camera.getMatrix(tMatrix);
            camera.restore();
            tMatrix.preTranslate(-halfWidth, -halfHeight);
            tMatrix.postTranslate(halfWidth, halfHeight);
            t.setAlpha(interpolatedTime < 0.5f ? 0.0f : 1.0f);
        }
    }

    public static class Builder {
        View outView;
        View inView;
        long durationMillis = DEFAULT_DURATION_MILLIS;
        Interpolator interpolator;
        Direction direction = Direction.LEFT;
        AnimationListener animationListener;

        private Builder() {

        }

        public Builder withOutView(@NonNull View outView) {
            this.outView = outView;
            return this;
        }

        public Builder withInView(@NonNull View inView) {
            this.inView = inView;
            return this;
        }

        public Builder withDurationMillis(long durationMillis) {
            this.durationMillis = durationMillis;
            return this;
        }

        public Builder withInterpolator(@NonNull Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public Builder withDirection(@NonNull Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder withAnimationListener(@NonNull AnimationListener animationListener) {
            this.animationListener = animationListener;
            return this;
        }

        void run() {
            if (outView == null || inView == null) {
                Log.d(TAG, "Cannot animate without an out view and an in view.  Aborting.");
                if (animationListener != null) {
                    animationListener.onAnimationFailed();
                }
                return;
            }
            new FlipAnimation(outView, inView, durationMillis, interpolator, direction, animationListener).run();
        }
    }

    public interface AnimationListener {
        void onAnimationStart(FlipAnimation flipAnimation);
        void onAnimationEnd(FlipAnimation flipAnimation);
        void onAnimationFailed();
    }
}
