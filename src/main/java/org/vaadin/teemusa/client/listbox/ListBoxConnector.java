package org.vaadin.teemusa.client.listbox;

import java.util.logging.Logger;

import org.vaadin.teemusa.beandatasource.client.HasDataSource;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.data.DataChangeHandler;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(org.vaadin.teemusa.listbox.ListBox.class)
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
		getWidget().clear();

		for (int i = 0; i < dataSource.size(); ++i) {
			JsonObject item = dataSource.getRow(i);
			if (item != null) {
				// We wrote the name provider output to "n"
				// Key is always stored at "k"
				getWidget().addItem(item.getString("n"), item.getString("k"));
			}
		}
	}
}
