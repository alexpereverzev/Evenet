package social.evenet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import social.evenet.R;
import social.evenet.pager.JazzyViewPager;
import social.evenet.pager.OutlineContainer;

public class CreateEventSuggest extends Fragment {

    private EditText name_place;
    private LinearLayout parent;
    private TranslateAnimation  mAnimation;
    private TextView label;
    private View view;
    private LinearLayout anima;
    private TextView title_place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_create_event_suggest, container, false);
        name_place=(EditText) view.findViewById(R.id.suggest_name);
        label=(TextView) view.findViewById(R.id.label);
        anima=(LinearLayout) view.findViewById(R.id.animation);

        title_place=(TextView) view.findViewById(R.id.title_place);

        name_place.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(name_place
                                    .getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    label.setVisibility(View.GONE);
                    parent.setAnimation(mAnimation);

                }
                return false;
            }
        });

        final Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.moving);
        name_place.startAnimation(anim1);
        parent=(LinearLayout) view.findViewById(R.id.parent);
       mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, -0.45f);
        mAnimation.setDuration(7000);
        mAnimation.setFillAfter(true);


        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anima.setVisibility(View.VISIBLE);
                title_place.setVisibility(View.VISIBLE);
                title_place.setText(name_place.getText());
                setupJazziness(JazzyViewPager.TransitionEffect.Tablet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




        return view;
    }

    private JazzyViewPager pager;

    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        pager = (JazzyViewPager) view.findViewById(R.id.jazzy_pager);
        pager.setTransitionEffect(effect);
        pager.setAdapter(new MainAdapter());
        pager.setPageMargin(30);

       /* pager.setPageMargin(-50);
        pager.setHorizontalFadingEdgeEnabled(true);
        pager.setFadingEdgeLength(30);*/
    }

    private class MainAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=   li.inflate(R.layout.create_place, container, false);

            container.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pager.setObjectForPosition(v, position);
            return v;
        }

      //  @Override public float getPageWidth(int position) { return(0.35f); }


        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(pager.findViewFromObject(position));
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }

}
