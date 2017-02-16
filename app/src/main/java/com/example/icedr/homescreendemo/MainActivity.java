package com.example.icedr.homescreendemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.example.icedr.homescreendemo.dao.DataDao;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.network.IDataLoadingResult;
import com.example.icedr.homescreendemo.widget.HomeCategoriesAdapter;
import com.example.icedr.homescreendemo.widget.managers.VerticalSmoothScrolledLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends Activity {

    @BindView(R.id.browse_list)
    RecyclerView browseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.category_component);
        ButterKnife.bind(this);
        loadProjects();
    }

    private void loadProjects() {
        DataDao.getInstance().getProjects(new IDataLoadingResult<List<Project>>() {
            @Override
            public void onResult(List<Project> projectList) {
                projectList.remove(0);
                browseList.setLayoutManager(new VerticalSmoothScrolledLayoutManager(MainActivity.this));
                browseList.setHasFixedSize(true);
                browseList.setItemAnimator(null);
                browseList.setNestedScrollingEnabled(false);
                browseList.setAdapter(new HomeCategoriesAdapter(projectList));
            }

            @Override
            public void onFailure(Throwable ex) {
                Intent intentError = new Intent(MainActivity.this, ErrorActivity.class);
                intentError.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentError.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentError.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intentError);
            }
        });
    }
}
