<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link media="screen" href="assets/main.css" type="text/css"
	rel="stylesheet" />
</head>
<body>
	<table width="100%">
		<tr>
			<td align="center" class="site-title" colspan="7">Ведический
				лунный календарь на 2012-2022 г.<br /> <span class="note">(время
					указано в часовом поясе Москвы GMT +03)</span>
			</td>
		</tr>
		<tr class="small-row">
			<td align="center" colspan="7">
				<table width="35%" class="navigation">
					<tr>
						<td align="center" class="previous" nowrap="nowrap"><c:choose>
								<c:when test="${!hidePrevious}">
									<a href="${calendar.getFilename(-1)}">
										&lt;-&nbsp;${calendar.getDisplayMonth(-1)} </a>
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose></td>
						<td align="center" class="current" nowrap="nowrap">
							${calendar.getDisplayMonth(0)}&nbsp;${calendar.getDisplayYear()}
						</td>
						<td align="center" class="next" nowrap="nowrap"><c:choose>
								<c:when test="${!hideNext}">
									<a href="${calendar.getFilename(1)}">
										${calendar.getDisplayMonth(1)}&nbsp;-&gt; </a>
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="week-days-row">
			<c:forEach items="${calendar.getWeekDays()}" var="weekDay">
				<td>
					<h3>
						<c:out value="${weekDay}" />
					</h3>
				</td>
			</c:forEach>
		</tr>

		<c:forEach items="${weeks}" var="week">
			<tr>
				<c:forEach items="${week}" var="day">
					<td valign="top"
						<c:if test="${day.getDayOfMonth() == current_day && day.getMonth() == current_month}">class="current-day"</c:if>
						width="14%">
						<h1
							class="<c:if test="${day.isEmpty()}">empty-day-title </c:if>day-title<c:if test="${!day.isEmpty()}"> moon-phase-${day.getPhase()}</c:if>">${day.getDayOfMonth()}</h1>
						<c:if test="${!day.isEmpty()}">
							<c:if test="${day.getEclipseView() != null}">
								<b> <c:if
										test="${day.getEclipseView().getGraha().getNumber() == 0}">
									Солнечное затмение
								</c:if> <c:if
										test="${day.getEclipseView().getGraha().getNumber() == 1}">
									Лунное затмение
								</c:if> (${day.getEclipseView().getFormattedTime()})
								</b>
								<br />
							</c:if>
							<c:forEach items="${day.getTithiViews()}" var="tithi">
								${tithi.getTithi()} л. с.
								<c:if test="${tithi.getDateTime() != null}">
									(${tithi.getFormattedTime()})
								</c:if>
								<br />
							</c:forEach>
							<c:forEach items="${day.getMoonRashiViews()}" var="moonRashi">
								Луна в<c:if test="${moonRashi.getRashi().getNumber() == 5}">о</c:if> ${moonRashi.getDisplayName()}
								<c:if test="${moonRashi.getDateTime() != null}">
									(${moonRashi.getFormattedTime()})
								</c:if>
								<br />
							</c:forEach>
							<c:forEach items="${day.getNakshatraViews()}" var="nakshatra">
								${nakshatra.getNakshatra().getNumber()}. ${nakshatra.getDisplayName()}
								<c:if test="${nakshatra.getDateTime() != null}">
									(${nakshatra.getFormattedTime()})
								</c:if>
								<br />
							</c:forEach>
							<c:forEach items="${day.getSunRashiViews()}" var="sunRashi">
								Солнце в ${sunRashi.getDisplayName()}
								<c:if test="${sunRashi.getDateTime() != null}">
									(${sunRashi.getFormattedTime()})
								</c:if>
							</c:forEach>
						</c:if>
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
</body>
</html>