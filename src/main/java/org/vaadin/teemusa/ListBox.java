package org.vaadin.teemusa;

import java.util.Collection;

import org.vaadin.teemusa.beandatasource.CollectionDataSource;
import org.vaadin.teemusa.beandatasource.DataProvider;

import com.vaadin.ui.AbstractComponent;

public class ListBox<T> extends AbstractComponent implements TypedComponent<T> {

	private DataProvider<T> dataProvider;

	public ListBox(Collection<T> values) {
		super();
		dataProvider = new CollectionDataSource<>(values).extend(this);
	}
}
