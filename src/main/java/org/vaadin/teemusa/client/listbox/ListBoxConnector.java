package org.vaadin.teemusa.client.listbox;

import org.vaadin.teemusa.beandatasource.client.HasDataSource;
import org.vaadin.teemusa.listbox.LBox;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.data.DataChangeHandler;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(LBox.class)
public class ListBoxConnector extends AbstractComponentConnector implements HasDataSource {

	private DataSource<JsonObject> dataSource;
	private ListBoxSelectRpc rpc;

	@Override
	public ListBox getWidget() {
		return (ListBox) super.getWidget();
	}

	@Override
	protected void init() {
		super.init();

		rpc = getRpcProxy(ListBoxSelectRpc.class);
		getWidget().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// Send selection to server
				rpc.select(getWidget().getSelectedValue());
			}
		});

		getWidget().addItem(" ", "");
		getWidget().setSelectedIndex(0);
	}

	@Override
	public void setDataSource(DataSource<JsonObject> rpcDataSource) {
		// Clean up old data source
		if (dataSource != null) {
			dataSource.setDataChangeHandler(null);
		}

		dataSource = rpcDataSource;
		dataSource.setDataChangeHandler(new DataChangeHandler() {

			@Override
			public void resetDataAndSize(int estimatedNewDataSize) {
				resetValues();
			}

			@Override
			public void dataUpdated(int firstRowIndex, int numberOfRows) {
				resetValues();
			}

			@Override
			public void dataRemoved(int firstRowIndex, int numberOfRows) {
				boolean resetSelection = false;

				// Offset by 1, due to empty item.
				int removedIndex = firstRowIndex + 1;

				for (int i = 0; i < numberOfRows && removedIndex < getWidget().getItemCount(); ++i) {
					if (!resetSelection && getWidget().getSelectedIndex() == removedIndex) {
						resetSelection = true;
					}
					getWidget().removeItem(removedIndex);
				}

				if (resetSelection) {
					getWidget().setSelectedIndex(0);
					// No event from setSelectedIndex, call manually.
					rpc.select("");
				}
			}

			@Override
			public void dataAvailable(int firstRowIndex, int numberOfRows) {
				resetValues();
			}

			@Override
			public void dataAdded(int firstRowIndex, int numberOfRows) {
				// Add new ones to end of list, no matter the actual place
				for (int i = 0; i < numberOfRows; ++i) {
					addItem(firstRowIndex + i);
				}
			}
		});
	}

	private void resetValues() {
		// Remove all non-empty items.
		while (getWidget().getItemCount() > 1) {
			getWidget().removeItem(1);
		}

		for (int i = 0; i < dataSource.size(); ++i) {
			addItem(i);
		}
		getWidget().setSelectedIndex(0);
	}

	protected void addItem(int index) {
		JsonObject item = dataSource.getRow(index);
		if (item != null) {
			// We wrote the name provider output to "n"
			// Key is always stored at "k"
			getWidget().addItem(item.getString("n"), item.getString("k"));
		}
	}
}
