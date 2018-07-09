package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private ImageView mSandwichIv;
    private TextView mAlsoKnownTv,mAlsoKnownLabel,mOriginTv,mOriginLabel,mDescriptionTv,mIngredientTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initUI();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            closeOnError();
            return;
        }

        populateUI(sandwich);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            mAlsoKnownTv.setText(stringBuilder.toString());
        } else {
            mAlsoKnownTv.setVisibility(View.GONE);
            mAlsoKnownLabel.setVisibility(View.GONE);
        }

        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            mOriginTv.setVisibility(View.GONE);
            mOriginLabel.setVisibility(View.GONE);
        } else {
            mOriginTv.setText(sandwich.getPlaceOfOrigin());
        }

        mDescriptionTv.setText(sandwich.getDescription());

        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getIngredients().get(0));

            for (int i = 1; i < sandwich.getIngredients().size(); i++) {
                stringBuilder.append(",");
                stringBuilder.append(sandwich.getIngredients().get(i));
            }
            mIngredientTv.setText(stringBuilder.toString());
        }

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mSandwichIv);
    }


public void initUI(){
    mSandwichIv = findViewById(R.id.image_iv);
    mAlsoKnownTv = findViewById(R.id.also_known_tv);
    mAlsoKnownLabel = findViewById(R.id.alsoKnownAs_txt);
    mOriginTv = findViewById(R.id.origin_tv);
    mOriginLabel = findViewById(R.id.placeOfOrigin_txt);
    mDescriptionTv = findViewById(R.id.description_tv);
    mIngredientTv = findViewById(R.id.ingredients_tv);
}
}