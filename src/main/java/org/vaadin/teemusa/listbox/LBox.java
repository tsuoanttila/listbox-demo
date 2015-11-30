package org.vaadin.teemusa.listbox;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.vaadin.teemusa.TypedComponent;
import org.vaadin.teemusa.beandatasource.CollectionDataSource;
import org.vaadin.teemusa.beandatasource.DataProvider;
import org.vaadin.teemusa.beandatasource.interfaces.DataGenerator;
import org.vaadin.teemusa.beandatasource.interfaces.DataSource;
import org.vaadin.teemusa.client.listbox.ListBoxSelectRpc;
import org.vaadin.teemusa.listbox.ValueChange.ValueChangeListener;
import org.vaadin.teemusa.listbox.ValueChange.ValueChangeNotifier;

import com.vaadin.ui.AbstractComponent;

import elemental.json.JsonObject;

public class LBox<T> extends AbstractComponent implements TypedComponent<T>, ValueChangeNotifier<T> {

	public interface NameProvider<T> {
		String getName(T value);
	}

	private final DataProvider<T> dataProvider;
	private T selected;
	private Collection<ValueChangeListener<T>> listeners = new LinkedHashSet<ValueChangeListener<T>>();

	public LBox(Collection<T> values, final NameProvider<T> nameProvider) {
		this(new CollectionDataSource<>(values), nameProvider);
	}

	public LBox(DataSource<T> dataSource, NameProvider<T> nameProvider) {
		dataProvider = dataSource.extend(this);
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
					// TODO: use fireEvent
					ValueChange<T> event = new ValueChange<T>(oldValue, selected);
					for (ValueChangeListener<T> listener : listeners) {
						listener.valueChange(event);
					}
				}
			}
		});
	}

	@Override
	public void addValueChangeListener(ValueChangeListener<T> listener) {
		// TODO: use proper event bus
		listeners.add(listener);
	}

	@Override
	public void removeValueChangeListener(ValueChangeListener<T> listener) {
		// TODO: use proper event bus
		listeners.remove(listener);
	}
}
