package com.palbang.unsemawang.fortune.dto.result.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "새해 신수 Response DTO")
public class SinsuResponse {

	// 올해의 행운
	private ThisYearLuck thisYearLuck;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ThisYearLuck {
		private Total total;
		private Business business;
		private Love love;
		private Health health;
		private TravelMoving travelMoving;
		private Work work;
		private MonthlyLuck monthlyLuck;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Total {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Business {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Love {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Health {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class TravelMoving {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Work {
			private String label;
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class MonthlyLuck {
			private String label;
			private List<Month> month;

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Month {
				private String label;
				private String value;
			}
		}
	}
}