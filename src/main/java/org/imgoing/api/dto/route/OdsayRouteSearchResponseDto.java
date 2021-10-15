package org.imgoing.api.dto.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OdsayRouteSearchResponseDto {
    private Result result;

    @AllArgsConstructor
    @Getter
    public static class Result {
        private int searchType;
        private int outTrafficCheck;
        private int busCount;
        private int subwayCount;
        private int subwayBusCount;
        private int pointDistance;
        private int startRadius;
        private int endRadius;
        private List<Path> path;

        @AllArgsConstructor
        @Getter
        public static class Path {
            private int pathType;
            private Info info;
            private List<SubPath> subPath;

            @AllArgsConstructor
            @Getter
            public static class Info {
                private double trafficDistance;
                private int totalWalk;
                private int totalTime;
                private int payment;
                private int busTransitCount;
                private int subwayTransitCount;
                private String mapObj;
                private String firstStartStation;
                private String lastEndStation;
                private int totalStationCount;
                private int busStationCount;
                private int subwayStationCount;
                private double totalDistance;
                private int totalWalkTime;
            }

            @AllArgsConstructor
            @Getter
            public static class SubPath {
                private int trafficType;
                private int distance;
                private int sectionTime;
                private int stationCount;
                private List<Lane> lane;
                private String startName;
                private double startX;
                private double startY;
                private String endName;
                private double endX;
                private double endY;
                private String way;
                private int wayCode;
                private String door;
                private int startID;
                private int endID;
                private String startExitNo;
                private double startExitX;
                private double startExitY;
                private String endExitNo;
                private double endExitX;
                private double endExitY;
                private PassStopList passStopList;

                @AllArgsConstructor
                @Getter
                public static class Lane {
                    private String name;
                    private int subwayCode;
                    private int subwayCityCode;
                    private String busNo;
                    private int type;
                    private int busID;
                }

                @AllArgsConstructor
                @Getter
                public static class PassStopList {
                    private List<Station> stations;

                    @AllArgsConstructor
                    @Getter
                    public static class Station {
                        private int index;
                        private int stationID;
                        private String stationName;
                        private String x;
                        private String y;
                        private String isNonStop;
                    }
                }
            }
        }
    }
}
