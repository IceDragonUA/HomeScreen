package com.example.icedr.homescreendemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.icedr.homescreendemo.dao.DataDao;
import com.example.icedr.homescreendemo.model.Project;
import com.example.icedr.homescreendemo.network.IDataLoadingResult;
import com.example.icedr.homescreendemo.widget.category.GridRecyclerView;
import com.example.icedr.homescreendemo.widget.category.HomeCategoriesAdapter;
import com.example.icedr.homescreendemo.widget.interfaces.OnItemPressedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @BindView(R.id.browse_list)
    GridRecyclerView browseList;

    private OnItemPressedListener onItemPressedListener = new OnItemPressedListener() {
        @Override
        public void onItemPressed(int positionX, int positionY) {
            Log.e(TAG, "onItemPressed: X = " + positionX + "; Y = " + positionY +";");
        }
    };

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
                browseList.createCoordinator(projectList.subList(0, 15));
                browseList.setAdapter(new HomeCategoriesAdapter(projectList.subList(0, 15)));
                browseList.getAdapter().setOnItemPressedListener(onItemPressedListener);
                browseList.setItemViewCacheSize(projectList.subList(0, 15).size());
                browseList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        browseList.setSelectedView(0);
                    }
                }, 500);
            }

            @Override
            public void onFailure(Throwable ex) {
                Log.e(TAG, "onFailure: ProjectList not loaded", ex);
            }
        });
    }
}
