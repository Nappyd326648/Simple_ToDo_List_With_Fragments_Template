package com.clara.simple_todo_list_with_fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
		AddToDoItemFragment.NewItemCreatedListener,
		ToDoItemDetailFragment.MarkItemAsDoneListener,
		ToDoListFragment.ListItemSelectedListener {


	private static final String TODO_ITEMS_KEY = "TODO ITEMS ARRAY LIST";
	private static final String ADD_NEW_FRAG_TAG = "ADD NEW FRAGMENT";
	private static final String LIST_FRAG_TAG = "LIST FRAGMENT";
	private static final String DETAIL_FRAG_TAG = "DETAIL FRAGMENT";

	private ArrayList<ToDoItem> mTodoItems;

	private static final String TAG = "MAIN ACTIVITY";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//TODO if no saved instance state, then app has just started
		//TODO create ArrayList, create Add and List fragments and add to this Activity
		if (savedInstanceState == null) {

			Log.d(TAG, "onCreate has no instance state. Set up ArrayList, add List Fragment and Add fragment");

			mTodoItems = new ArrayList<>();

			AddToDoItemFragment addNewFragment = AddToDoItemFragment.newInstance();
			ToDoListFragment listFragment = ToDoListFragment.newInstance(mTodoItems);

			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			ft.add(R.id.add_todo_view_container, addNewFragment, ADD_NEW_FRAG_TAG);
			ft.add(R.id.todo_list_view_container, listFragment, LIST_FRAG_TAG);

			ft.commit();

		} else {


			mTodoItems = savedInstanceState.getParcelableArrayList(TODO_ITEMS_KEY);
			Log.d(TAG, "onCreate has saved instance state ArrayList =  " + mTodoItems);
		}


		//TODO else, if there is saved instance state, then app is currently running
		//TODO get ArrayList from saved instance state

	}

	@Override
	public void onSaveInstanceState(Bundle outBundle) {
		//TODO save ArrayList in outBundle
		super.onSaveInstanceState(outBundle);
		outBundle.putParcelableArrayList(TODO_ITEMS_KEY, mTodoItems);
	}

	@Override
	public void newItemCreated(ToDoItem newItem) {
		//todo Add new item to list, notify ToDoListFragment that it needs to update
		mTodoItems.add(newItem);

		Log.d(TAG, "newItemCreated =  " + mTodoItems);

		//get reference to list Fragment from the FragmentMananger,
		// and tell this Fragment that the data set has changed
		FragmentManager fm = getFragmentManager();
		ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
		listFragment.notifyItemsChanged();
	}

	@Override
	public void itemSelected(ToDoItem selected) {
		//todo Show ToDoDetailFragment for this Todo item
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		//Create a new Detail fragment. Add it to the Activity.
		ToDoItemDetailFragment detailFragment = ToDoItemDetailFragment.newInstance(selected);
		ft.add(android.R.id.content, detailFragment);
		// Add to the back stack, so if user presses back button from the Detail
		// fragment, it will revert this transaction - Activity will go back to the Add+List fragments
		ft.addToBackStack(DETAIL_FRAG_TAG);

		ft.commit();
	}

	@Override
	public void todoItemDone(ToDoItem doneItem) {
		//todo remove from ArrayList.
		//todo If showing ToDoItemDetailFragment, go back to List+Add view.
		mTodoItems.remove(doneItem);

		Log.d(TAG, "newItemRemoved list is now  =  " + mTodoItems);

		//Find List fragment and tell it that the  data has changed
		FragmentManager fm = getFragmentManager();
		ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
		listFragment.notifyItemsChanged();

		// Revert the last fragment transaction on the back stack.
		// This removes the Detail fragment from the Activity, which leaves the Add+List fragments.

		FragmentTransaction ft = fm.beginTransaction();
		fm.popBackStack();
		ft.commit();
	}
}


