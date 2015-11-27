package org.vaadin.teemusa.client.listbox;

import com.vaadin.shared.communication.ServerRpc;

public interface ListBoxSelectRpc extends ServerRpc {

	void select(String key);

}
