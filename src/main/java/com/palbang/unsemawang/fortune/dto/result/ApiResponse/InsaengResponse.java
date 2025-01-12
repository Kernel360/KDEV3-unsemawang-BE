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
	private Total total;

	// 궁합
	private Gunghap gunghap;

	// 결혼 관련 정보
	private MarryGung marryGung;

	// 행운
	private Luck luck;

	// 별자리
	private Constellation constellation;

	// 현재의 길흉사
	private CurrentGoodAndBadNews currentGoodAndBadNews;

	// 팔복궁
	private EightStar eightStar;

	// 풍수로 보는 길흉
	private GoodAndBadByPungsu goodAndBadByPungsu;

	// 천생연분
	private SoulMates soulMates;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Total {
		private String label;
		private Text text;
		private Value value;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Text {
			private String labe;
			private String value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Value {
			private String label;
			private int value;
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Gunghap {
		private String label;
		private Text text;
		private Value value;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Text {
			private String label;
			private String value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Value {
			private String label;
			private int value;
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MarryGung {
		private String label;
		private Text text;
		private Value value;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Text {
			private String label;
			private String value;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Value {
			private String label;
			private int value;
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Luck {
		private String label;
		private List<Children> children;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Children {
			private Personality personality;
			private Job job;
			private Health health;

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Personality {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Job {
				private String label;
				private String value;
			}

			@Data
			@NoArgsConstructor
			@AllArgsConstructor
			public static class Health {
				private String label;
				private String value;
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Constellation {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CurrentGoodAndBadNews {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EightStar {
		private String labe;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GoodAndBadByPungsu {
		private String label;
		private String value;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SoulMates {
		private String label;
		private String value;
	}
}