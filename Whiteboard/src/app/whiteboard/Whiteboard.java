package app.whiteboard;

import net.cellcloud.common.Logger;
import net.cellcloud.core.Cellet;
import net.cellcloud.core.CelletFeature;
import net.cellcloud.core.CelletVersion;
import net.cellcloud.talk.Primitive;
import net.cellcloud.talk.dialect.ActionDialect;

public class Whiteboard extends Cellet {

	private Dispatcher dispatcher;

	public Whiteboard() {
		super(new CelletFeature("Whiteboard", new CelletVersion(1, 0, 0)));

		this.dispatcher = new Dispatcher(this);
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
		SessionManager.getInstance().stop();
	}

	@Override
	public void dialogue(String tag, Primitive primitive) {
		if (primitive.isDialectal()) {
			ActionDialect dialect = (ActionDialect) primitive.getDialect();
			this.dispatcher.dispatch(dialect);
		}
		else {
			Logger.e(this.getClass(), "dialogue data error");
		}
	}
}
