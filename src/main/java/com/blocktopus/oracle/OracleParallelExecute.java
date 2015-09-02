package com.blocktopus.oracle;

import static com.blocktopus.common.CollectionUtils.newList;

import java.util.List;

import com.blocktopus.oracle.types.LiteralString;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;

public class OracleParallelExecute {

	public static class ParallelTask {
		public ParallelTask() {}
		public ParallelTask(String task, Chunker chunk) {
			super();
			this.task = task;
			this.chunk = chunk;
		}

		public static interface Chunker {
			public String getChunkSQL();
		}

		public static class LongIDChunker implements Chunker {
			List<List<Long>> longChunks = newList();

			public void addChunk(Long from, Long to) {
				List<Long> chunk = newList();
				chunk.add(from);
				chunk.add(to);
				longChunks.add(chunk);
			}

			public String getChunkSQL() {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < longChunks.size(); i++) {
					if (i > 0) {
						sb.append(" union ");
					}
					sb.append("select " + longChunks.get(i).get(0) + ","
							+ longChunks.get(i).get(1) + " from dual");
				}
				return sb.toString();
			}
		}

		public static class RowIDChunker implements Chunker {
			List<List<String>> rowIDChunks = newList();

			public void addChunk(String from, String to) {
				List<String> chunk = newList();
				chunk.add(from);
				chunk.add(to);
				rowIDChunks.add(chunk);
			}

			public String getChunkSQL() {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < rowIDChunks.size(); i++) {
					if (i > 0) {
						sb.append(" union ");
					}
					sb.append("select " + rowIDChunks.get(i).get(0) + ","
							+ rowIDChunks.get(i).get(1) + " from dual");
				}
				return sb.toString();
			}
		}

		private String task;
		private Chunker chunk;

		public String getTask() {
			// change to the oracle convention.
			return task.replace("?", ":id");
		}

		public void setTask(String task) {
			int count = task.length() - task.replace("?", "").length();
			if (count != 2) {
				throw new RuntimeException("Parallel task must contain exactly 2 bind variables(?) for from and to.");
			}

			this.task = task;
		}

		public Chunker getChunk() {
			return chunk;
		}

		public void setChunk(Chunker chunk) {
			this.chunk = chunk;
		}

		public String getChunkSQL() {
			return chunk.getChunkSQL();
		}
	}

	private OracleCodeCaller oracleCodeCaller;

	public OracleCodeCaller getOracleCodeCaller() {
		if (oracleCodeCaller == null) {
			throw new RuntimeException("Must give configured OracleCodeCaller before use");
		}
		return oracleCodeCaller;
	}

	public void setOracleCodeCaller(OracleCodeCaller oracleCodeCaller) {
		this.oracleCodeCaller = oracleCodeCaller;
	}

	public void runMutiThreadTask(ParallelTask pt) {

		PrimitiveOutputParameter<String> taskName = ParameterFactory.getVarcharOutputParameter();
		getOracleCodeCaller().callFunction("DBMS_PARALLEL_EXECUTE.generate_task_name",
				taskName);

		getOracleCodeCaller().callStoredProcedure("DBMS_PARALLEL_EXECUTE.create_task",
				taskName.getParameter());

		getOracleCodeCaller().callStoredProcedure("DBMS_PARALLEL_EXECUTE.create_chunks_by_sql",
				taskName.getParameter(),
				pt.getChunkSQL(),
				new LiteralString("false"));

		getOracleCodeCaller().callStoredProcedure("DBMS_PARALLEL_EXECUTE.run_task",
				taskName.getParameter(),
				pt.getTask(),
				new LiteralString("DBMS_SQL.native"),
				null,
				null,
				new LiteralString("TRUE"),
				10);

	}

}
