package fr.alex.games.box2d.entities;

public class ComponentEvent {
	private Component source;
	private Object param;
	private EventType type;
	private boolean handled;

	public ComponentEvent(Component source, Object param, EventType type) {
		super();
		this.source = source;
		this.param = param;
		this.type = type;
		this.handled = false;
	}

	public Component getSource() {
		return source;
	}

	public Object getParam() {
		return param;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public EventType getType() {
		return type;
	}
}
