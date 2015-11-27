package org.vaadin.teemusa.listbox;

import java.util.Collection;

import org.vaadin.teemusa.TypedComponent;
import org.vaadin.teemusa.beandatasource.CollectionDataSource;
import org.vaadin.teemusa.beandatasource.DataProvider;
import org.vaadin.teemusa.beandatasource.interfaces.DataGenerator;
import org.vaadin.teemusa.client.listbox.ListBoxSelectRpc;

import com.vaadin.ui.AbstractComponent;

import elemental.json.JsonObject;

public class ListBox<T> extends AbstractComponent implements TypedComponent<T> {

	public interface NameProvider<T> {
		String getName(T value);
	}

	private DataProvider<T> dataProvider;
	private T selected;

	public ListBox(Collection<T> values, final NameProvider<T> nameProvider) {
		super();
		dataProvider = new CollectionDataSource<>(values).extend(this);
		dataProvider.addDataGenerator(new DataGenerator<T>() {

			@Override
			public void generateData(T bean, JsonObject rowData) {
				rowData.put("n", nameProvider.getName(bean));
			}

			@Override
			public void destroyData(T bean) {
				// No data needs to be destroyed.
			}
		});

		registerRpc(new ListBoxSelectRpc() {

			@Override
			public void select(String key) {
				T oldValue = selected;
				selected = dataProvider.getKeyMapper().get(key);
				if (oldValue != selected) {
					// TODO: fire event
				}
			}
		});
	}
}
