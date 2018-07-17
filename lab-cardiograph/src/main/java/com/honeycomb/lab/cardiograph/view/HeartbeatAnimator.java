package com.honeycomb.lab.cardiograph.view;

import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.honeycomb.lib.utilities.state.State;
import com.honeycomb.lib.utilities.state.StateMachine;

public class HeartbeatAnimator {
    private final ImageView mImageView;
    private final Animation mHeartbeatAnimation;

    private final StateMachine mStateMachine;
    private final IdleState mIdle;
    private final BeatingState mBeating;

    private static final int BEATING_COLOR = Color.parseColor("#FF6868");
    private static final int IDLE_COLOR = Color.parseColor("#BBBBBB");

    public HeartbeatAnimator(ImageView imageView) {
        mImageView = imageView;
        mHeartbeatAnimation = createHeartbeatAnimation();

        mStateMachine = new StateMachine();
        mIdle = new IdleState(this);
        mBeating = new BeatingState(this);
    }

    public void idle() {
        mStateMachine.setState(mIdle);
    }

    public void beat() {
        mStateMachine.setState(mBeating);
    }

    void updateHeartbeatColor(boolean beating) {
        int color = beating ? BEATING_COLOR : IDLE_COLOR;
        mImageView.setColorFilter(color);
    }

    void startBeating() {
        mImageView.startAnimation(mHeartbeatAnimation);
    }

    private static final float SCALE = 1.5F;

    private Animation createHeartbeatAnimation() {
        Animation animation = new ScaleAnimation(1.0F, SCALE, 1.0F, SCALE,
                Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        animation.setDuration(200);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                idle();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return animation;
    }

    private static class IdleState extends HeartbeatState {

        IdleState(HeartbeatAnimator animator) {
            super(animator);
        }

        @Override
        public void onEnter() {
            mAnimator.updateHeartbeatColor(false);
        }

    }

    private static class BeatingState extends HeartbeatState {

        BeatingState(HeartbeatAnimator animator) {
            super(animator);
        }

        @Override
        public void onEnter() {
            mAnimator.updateHeartbeatColor(true);
            mAnimator.startBeating();
        }
    }

    private static class HeartbeatState extends State {
        final HeartbeatAnimator mAnimator;

        HeartbeatState(HeartbeatAnimator animator) {
            mAnimator = animator;
        }
    }
}
