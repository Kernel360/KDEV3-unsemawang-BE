package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "인생풀이 Response DTO")
public class InsaengResponse {
	// 총평
	@Schema(required = true)
	private Total total;

	// 궁합
	@Schema(required = true)
	private Gunghap gunghap;

	// 결혼 관련 정보
	@Schema(required = true)
	private MarryGung marryGung;

	// 행운
	@Schema(required = true)
	private Luck luck;

	// 별자리
	@Schema(required = true)
	private Constellation constellation;

	// 현재의 길흉사
	@Schema(required = true)
	private CurrentGoodAndBadNews currentGoodAndBadNews;

	// 팔복궁
	@Schema(required = true)
	private EightStar eightStar;

	// 풍수로 보는 길흉
	@Schema(required = true)
	private GoodAndBadByPungsu goodAndBadByPungsu;

	// 천생연분
	@Schema(required = true)
	private SoulMates soulMates;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Total {
		@Schema(required = true)
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Text text;
			@Schema(required = true)
			private Value value;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Text {
				@Schema(required = true)
				private String labe;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Value {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private int value;
			}
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Gunghap {
		@Schema(required = true)
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			private Text text;
			@Schema(required = true)
			private Value value;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Text {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Value {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private int value;
			}
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MarryGung {
		@Schema(required = true)
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Text text;
			@Schema(required = true)
			private Value value;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Text {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Value {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private int value;
			}
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			@Schema(required = true)
			private Personality personality;
			@Schema(required = true)
			private Job job;
			@Schema(required = true)
			private Health health;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Personality {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Job {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Health {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Constellation {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentGoodAndBadNews {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EightStar {
		@Schema(required = true)
		private String labe;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GoodAndBadByPungsu {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SoulMates {
		@Schema(required = true)
		private String label;
		@Schema(required = true)
		private String value;
	}
}