/*
 * Copyright 2013 Chris Banes
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

package uk.co.senab.actionbarpulltorefresh.samples.actionbarsherlock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.FragmentHelper;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * This sample shows how to use ActionBar-PullToRefresh with a
 * {@link android.widget.ListView ListView}, and manually creating (and attaching) a
 * {@link PullToRefreshLayout} to the view.
 */
public class ListViewActivity extends BaseSampleActivity {

    @Override
    protected Fragment getSampleFragment() {
        return new SampleListFragment();
    }

    /**
     * Fragment Class
     */
    public static class SampleListFragment extends ListFragment implements
            OnRefreshListener {

        private static String[] ITEMS = {"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
                "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
                "Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler", "Abbaye de Belloc",
                "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi", "Acorn", "Adelost",
                "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
                "Allgauer Emmentaler"};

        private PullToRefreshLayout mPullToRefreshLayout;

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view,savedInstanceState);

            // As we're using a ListFragment we need to 'inject' a PullToRefreshLayout into it.
            // This is easily done with FragmentHelper
            mPullToRefreshLayout = new PullToRefreshLayout(getActivity());
            FragmentHelper.insertIntoFragmentView(view, mPullToRefreshLayout);

            // Now setup the PullToRefreshLayout as normal
            mPullToRefreshLayout.setup(getActivity())
                    .theseViewsAreRefreshable(android.R.id.list, android.R.id.empty)
                    .withListener(this)
                    .done();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Set the List Adapter to display the sample items
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, ITEMS));
            setListShownNoAnimation(true);
        }

        @Override
        public void onRefreshStarted(View view) {
            // Hide the list
            setListShown(false);

            /**
             * Simulate Refresh with 4 seconds sleep
             */
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(Constants.SIMULATED_REFRESH_LENGTH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);

                    // Notify PullToRefreshLayout that the refresh has finished
                    mPullToRefreshLayout.setRefreshComplete();
                    // Show the list again
                    setListShown(true);
                }
            }.execute();
        }
    }
}
