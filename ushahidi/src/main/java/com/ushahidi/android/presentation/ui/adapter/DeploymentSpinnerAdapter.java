package com.ushahidi.android.presentation.ui.adapter;

import com.ushahidi.android.R;
import com.ushahidi.android.presentation.model.DeploymentModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Deployment Spinner Adapter
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentSpinnerAdapter extends ArrayAdapter<String> {

    private final static int LAYOUT_RESOURCE_ID = R.layout.deployment_spinner_item;

    private final List<DeploymentModel> mDeploymentModels = new ArrayList<>();

    private final Context mContext;

    public DeploymentSpinnerAdapter(Context context, List<DeploymentModel> deploymentModels) {
        super(context, LAYOUT_RESOURCE_ID);
        setItems(deploymentModels);
        setDropDownViewResource(R.layout.deployment_spinner_dropdown_item);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Widgets widget;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(LAYOUT_RESOURCE_ID,
                    parent, false);
            widget = new Widgets(convertView);
            convertView.setTag(widget);
        } else {
            widget = (Widgets) convertView.getTag();
        }

        widget.title.setText(getDeploymentModels().get(position).getTitle());
        return convertView;
    }

    public List<DeploymentModel> getDeploymentModels() {
        return mDeploymentModels;
    }

    public void setItems(List<DeploymentModel> deploymentModels) {

        for (DeploymentModel deploymentModel : deploymentModels) {
            add(deploymentModel.getTitle(), deploymentModel);
        }
    }

    @Override
    public void clear() {
        super.clear();
        mDeploymentModels.clear();
    }

    @Override
    public void add(String label) {
        super.add(label);
        mDeploymentModels.add(null);
    }

    public void add(String label, DeploymentModel deploymentModel) {
        super.add(label);
        mDeploymentModels.add(deploymentModel);
    }

    @Override
    public void insert(String object, int index) {
        super.insert(object, index);
    }

    private class Widgets {

        TextView title;

        public Widgets(View convertView) {
            title = (TextView) convertView.findViewById(android.R.id.text1);
        }
    }
}
