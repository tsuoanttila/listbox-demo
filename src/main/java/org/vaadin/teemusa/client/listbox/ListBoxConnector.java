package org.vaadin.teemusa.client.listbox;

import org.vaadin.teemusa.beandatasource.client.HasDataSource;

import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.data.DataChangeHandler;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(org.vaadin.teemusa.listbox.ListBox.class)
public class ListBoxConnector extends AbstractComponentConnector implements HasDataSource {

	private DataSource<JsonObject> dataSource;

	@Override
	public ListBox getWidget() {
		return (ListBox) super.getWidget();
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
				resetValues();
			}

			@Override
			public void dataAvailable(int firstRowIndex, int numberOfRows) {
				resetValues();
			}

			@Override
			public void dataAdded(int firstRowIndex, int numberOfRows) {
				resetValues();
			}
		});
	}

	private void resetValues() {
		// Here we're supposed to reset all the data in the ListBox widget
	}
}
