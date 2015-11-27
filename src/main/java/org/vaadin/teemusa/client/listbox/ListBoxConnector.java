package org.vaadin.teemusa.client.listbox;

import org.vaadin.teemusa.beandatasource.client.HasDataSource;

import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.data.DataSource;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(org.vaadin.teemusa.listbox.ListBox.class)
public class ListBoxConnector extends AbstractComponentConnector implements HasDataSource {

	@Override
	public ListBox getWidget() {
		return (ListBox) super.getWidget();
	}

	@Override
	public void setDataSource(DataSource<JsonObject> rpcDataSource) {
		// TODO Auto-generated method stub
	}
}
