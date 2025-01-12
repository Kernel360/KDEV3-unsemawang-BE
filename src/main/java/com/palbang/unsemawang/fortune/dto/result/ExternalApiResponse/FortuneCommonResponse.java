package com.palbang.unsemawang.fortune.dto.result.ExternalApiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FortuneCommonResponse {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OhaengAnalysis {
		private String inbornOhaeng;
		private List<OhaengSpirit> spirit;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class OhaengSpirit {
			private String type;
			private int value;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Sajumyeongsik {
		private String type;
		private String koreanType;
		private Cheongan cheongan;
		private Jiji jiji;
		private List<String> sinsal;

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Cheongan {
			private String chinese;
			private String korean;
			private String text;
		}

		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Jiji {
			private String chinese;
			private String korean;
			private String text;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Wealth {
		private String characteristics;
		private String accumulate;
		private String prevent;
		private String investment;
		private String currentLuck;
	}
}