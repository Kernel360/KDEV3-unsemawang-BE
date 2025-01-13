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
	@Schema(required = true)
	private ThisYearLuck thisYearLuck;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ThisYearLuck {
		@Schema(required = true)
		private Total total;
		@Schema(required = true)
		private Business business;
		@Schema(required = true)
		private Love love;
		@Schema(required = true)
		private Health health;
		@Schema(required = true)
		private TravelMoving travelMoving;
		@Schema(required = true)
		private Work work;
		@Schema(required = true)
		private MonthlyLuck monthlyLuck;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Total {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Business {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Love {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Health {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class TravelMoving {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Work {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private String value;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class MonthlyLuck {
			@Schema(required = true)
			private String label;
			@Schema(required = true)
			private List<Month> month;

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			public static class Month {
				@Schema(required = true)
				private String label;
				@Schema(required = true)
				private String value;
			}
		}
	}
}