package recoding.hackathon.com.flugproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by KSH on 2017-06-16.
 */

public class TutorialFragments extends Fragment {
    private Context context;
    private int page;

    public TutorialFragments(){

    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setPage(int position){
        page = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.tutorial_fragments, container, false);

        ImageView slideboxImg = (ImageView)layout.findViewById(R.id.slidebox_img);
        TextView slideboxTitle = (TextView)layout.findViewById(R.id.slidebox_title);
        TextView slideboxTxt = (TextView)layout.findViewById(R.id.slidebox_txt);
        Button slideboxBtn = (Button)layout.findViewById(R.id.slidebox_button);

        switch (page){
            case 0:
                slideboxImg.setImageResource(R.mipmap.slidebox1);
                slideboxTitle.setText(R.string.welcome);
                slideboxTxt.setText(R.string.welcome_txt);
                slideboxBtn.setVisibility(View.GONE);
                slideboxTxt.setVisibility(View.VISIBLE);
                break;
            case 1:
                slideboxImg.setImageResource(R.mipmap.slidebox2);
                slideboxTitle.setText(R.string.how);
                slideboxTxt.setText(R.string.how_txt);
                slideboxBtn.setVisibility(View.GONE);
                slideboxTxt.setVisibility(View.VISIBLE);
                break;
            case 2:
                slideboxImg.setImageResource(R.mipmap.slidebox3);
                slideboxTitle.setText(R.string.how_p);
                slideboxTxt.setText(R.string.how_p_txt);
                slideboxBtn.setVisibility(View.GONE);
                slideboxTxt.setVisibility(View.VISIBLE);
                break;
            case 3:
                slideboxImg.setImageResource(R.mipmap.slidebox4);
                slideboxTitle.setText(R.string.ready);
                slideboxBtn.setText(R.string.btn_continue);
                slideboxBtn.setVisibility(View.VISIBLE);
                slideboxTxt.setVisibility(View.GONE);
                slideboxBtn.setOnClickListener(continueListener);
                break;
        }
        return layout;
    }

    View.OnClickListener continueListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    };
}
