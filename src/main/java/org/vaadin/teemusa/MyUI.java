package org.vaadin.teemusa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.vaadin.teemusa.listbox.LBox;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Widgetset("org.vaadin.teemusa.MyAppWidgetset")
public class MyUI extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		LBox<Reservation> listBox = new LBox<>(Reservation.generateReservations(), Reservation::getReservationName);
		listBox.addValueChangeListener(event -> {
			if (event.getNewValue() != null) {
				Notification.show("Selected Reservation for " + event.getNewValue().getReservationName());
			} else {
				Notification.show("Selection cleared");
			}
		});

		layout.addComponent(listBox);
		layout.addComponent(
				new Button("Remove reservation", event -> listBox.getDataSource().remove(listBox.getSelected())));

		TextField name = new TextField("Reservation Name");
		Button button = new Button("Add reservation", event -> {
			listBox.getDataSource().add(new Reservation(name.getValue()));
			name.clear();
		});
		HorizontalLayout hLayout = new HorizontalLayout(name, button);
		layout.addComponent(hLayout);

		layout.setSizeFull();
		layout.setComponentAlignment(hLayout, Alignment.BOTTOM_LEFT);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(button, Alignment.BOTTOM_RIGHT);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}

class Reservation {

	private String reservationName;

	public Reservation(String reservationName) {
		this.setReservationName(reservationName);
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	public static Collection<Reservation> generateReservations() {
		List<String> names = Arrays.asList("Teemu", "Henri", "Johannes", "Sami", "Anssi", "Markus", "Ilya");
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (String name : names) {
			reservations.add(new Reservation(name));
		}
		return reservations;
	}
}
