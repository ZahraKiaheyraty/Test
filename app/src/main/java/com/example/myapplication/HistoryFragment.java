package com.example.myapplication;

import android.os.Bundle;
import android.text.Spanned;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;
import com.example.myapplication.R;

/**
 * This fragment displays the history information for a client
 *
 */
public class HistoryFragment extends ListFragment {

  /** Client handle to a {@link Connection} object **/
  String clientHandle = null;
  /** {@link ArrayAdapter} to display the formatted text **/
  ArrayAdapter<Spanned> arrayAdapter = null;

  /**
   * @see ListFragment#onCreate(Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Pull history information out of bundle

    clientHandle = getArguments().getString("handle");
    Connection connection = Connections.getInstance(getActivity()).getConnection(clientHandle);

    Spanned[] history = connection.history();

    //Initialise the arrayAdapter, view and add data
    arrayAdapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.list_view_text_view);

    arrayAdapter.addAll(history);
    setListAdapter(arrayAdapter);

  }

  /**
   * Updates the data displayed to match the current history
   */
  public void refresh() {
    if (arrayAdapter != null) {
      arrayAdapter.clear();
      arrayAdapter.addAll(Connections.getInstance(getActivity()).getConnection(clientHandle).history());
      arrayAdapter.notifyDataSetChanged();
    }

  }

}
