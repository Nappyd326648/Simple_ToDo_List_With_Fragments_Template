package com.clara.simple_todo_list_with_fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Displays a list of to do items. Responds to list updates.
 */

public class ToDoListFragment extends Fragment {

	//private ListItemSelectedListener mListener;

	private static final String TAG = "TODO LIST FRAGMENT" ;
	private static final String TODO_LIST_ARGS ="to do list arguments";
	private ListItemSelectedListener mItemSelectedListener;

	private ToDoListArrayAdapter mListAdapter;
	//todo create newInstance method to send arguments to this Fragment

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

		ListView mListView = (ListView) view.findViewById(R.id.to_do_listview);
		ArrayList<ToDoItem> listItems = getArguments().getParcelableArrayList(TODO_LIST_ARGS);
		Log.d(TAG,"onCreateView, ArrayList:" + listItems);
		 mListAdapter = new ToDoListArrayAdapter(getActivity(),R.layout.todo_list_item_list_element,listItems);

		//todo Configure ListView with list of ToDo items
		//todo Use custom ToDoListArrayAdapter
		mListView.setAdapter(mListAdapter);
		mListAdapter.notifyDataSetChanged();

		if (getActivity() instanceof ListItemSelectedListener) {
			mItemSelectedListener = (ListItemSelectedListener) getActivity();
		} else {
			throw new RuntimeException(getActivity().toString()
					+ " must implement OnFragmentInteractionListener");
		}
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Notify the listener that the user has clicked on a list item. Send the item clicked on.
				Log.d(TAG, "List item " + position + " clicked, the todo item is " + mListAdapter.getItem(position) );
				mItemSelectedListener.itemSelected(mListAdapter.getItem(position));
			}
		});

		return view;
	}

	//todo create a method for notifying this Fragment about list updates

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof ListItemSelectedListener) {
			mItemSelectedListener = (ListItemSelectedListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mItemSelectedListener = null;
	}

	public void notifyItemsChanged() {
		Log.d(TAG, "Notified that the list of to do items has changed, update view");
		//Tell the list to update.
		mListAdapter.notifyDataSetChanged();
	}
	public interface ListItemSelectedListener {
		void itemSelected(ToDoItem selected);
	}

	public static ToDoListFragment newInstance(ArrayList todoItem){
		final Bundle args = new Bundle();
		args.putParcelableArrayList(TODO_LIST_ARGS,todoItem);
		final ToDoListFragment fragment = new ToDoListFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
