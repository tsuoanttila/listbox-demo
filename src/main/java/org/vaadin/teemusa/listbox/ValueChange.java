package org.vaadin.teemusa.listbox;

public class ValueChange<T> {

	private final T newValue;
	private final T oldValue;

	public ValueChange(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public T getNewValue() {
		return newValue;
	}

	public T getOldValue() {
		return oldValue;
	}

	public interface ValueChangeListener<T> {

		void valueChange(ValueChange<T> event);

	}

	public interface ValueChangeNotifier<T> {

		void addValueChangeListener(ValueChangeListener<T> listener);

		void removeValueChangeListener(ValueChangeListener<T> listener);
	}

}
