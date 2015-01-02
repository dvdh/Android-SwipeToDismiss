/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.swipedismiss;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    static final int MAX_MOCK_DATA = 20;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter<CustomViewHolder> mAdapter;

    ArrayList<String> mMockData;

    Toast mToast;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMockData();

        setContentView(R.layout.activity_main);

        //init RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RecyclerView.Adapter<CustomViewHolder>() {
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View view = inflater.inflate(R.layout.sample_row, viewGroup, false);
                return new CustomViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CustomViewHolder viewHolder, int i) {
                viewHolder.updateItem(mMockData.get(i));
            }

            @Override
            public int getItemCount() {
                return mMockData.size();
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        //setup swipe dismiss touch listener
        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int position) {
                                mMockData.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        });
        mRecyclerView.addOnItemTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during RecyclerView scrolling,
        // we don't look for swipes.
        mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());
    }

    private void initMockData() {
        mMockData = new ArrayList<String>(MAX_MOCK_DATA);
        for (int i = 0; i < MAX_MOCK_DATA; i++) {
            mMockData.add("Item " + (i + 1));
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder
                                    implements View.OnClickListener {
        TextView mTextView;
        String data;

        public CustomViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(this);
        }

        public void updateItem(String data) {
            this.data = data;
            mTextView.setText(data);
        }

        @Override
        public void onClick(View v) {
            if (mToast != null) {
                mToast.cancel();
            }

            mToast = Toast.makeText(MainActivity.this, "Clicked " + data, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
