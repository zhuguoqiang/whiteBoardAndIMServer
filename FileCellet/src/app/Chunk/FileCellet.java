package app.Chunk;

//import cube.common.User;
//import cube.common.UserCenter;
import app.Chunk.FileManager;
import app.Chunk.Dispatcher;
import net.cellcloud.common.Logger;
import net.cellcloud.core.Cellet;
import net.cellcloud.core.CelletFeature;
import net.cellcloud.core.CelletVersion;
import net.cellcloud.talk.Primitive;
import net.cellcloud.talk.dialect.Dialect;
import net.cellcloud.talk.dialect.ActionDialect;
import net.cellcloud.talk.dialect.ChunkDialect;

public class FileCellet extends Cellet {

	private Dispatcher dispatcher;

	public FileCellet() {
		super(new CelletFeature("Dummy", new CelletVersion(1, 0, 0)));

		this.dispatcher = new Dispatcher(this);
	}
	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}
	@Override
	public void dialogue(String tag, Primitive primitive) {
		if (primitive.isDialectal()) {
			Dialect dialect = (Dialect) primitive.getDialect();
			if (dialect instanceof ActionDialect) {
				this.dispatcher.dispatch((ActionDialect)dialect);
			}
			else if (dialect instanceof ChunkDialect) {
				this.process((ChunkDialect) dialect);
			}
			
		}
	}
	
	private void process(ChunkDialect dialect) {
		String tag = dialect.getOwnerTag();
		String tracker = dialect.getTracker();

		FileManager.getInstance().receiveChunk("sender", "receiver", dialect);
	}
}
