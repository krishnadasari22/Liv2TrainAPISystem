package com.televisory.bond.dto;

import java.util.Date;

import org.dozer.Mapping;

public class CDSDataDTO {
	

	@Mapping("cdsDataDetails.entityName")
	private String entityName;
	
	@Mapping("cdsDataDetails.currency")
	private String currency;
	
	
	@Mapping("cdsDataDetails.tenor")
	private Double tenor;
	
	private String identifier;
	
	private Date maturityDate;

	private Date businessDateTime;

	private String iceCdsCode;

	private String bbgCdsTicker;

	private Integer cmaEntityId;

	private String cmaTicker;

	private String contractStandard;

	private String sector;

	private String region;

	@Mapping("cdsDataDetails.seniority")
	private String seniority;

	@Mapping("cdsDataDetails.restructuringType")
	private String restructuringType;

	private Integer coupon;

	private String marketQuotingConvention;

	private String instrumentType;

	private Date defaultDate;

	private Date auctionDate;

	private Double parSpreadBid;

	private Double parSpreadMid;

	private Double parSpreadOffer;

	private Double quoteSpreadBid;

	private Double quoteSpreadMid;

	private Double quoteSpreadOffer;

	private Double upFrontBid;

	private Double upFrontMid;

	private Double upFrontOffer;

	private Double percentOfParBid;

	private Double percentOfParMid;

	private Double percentOfParOffer;

	private Double parSpreadPv01Bid;

	private Double parSpreadPv01Mid;

	private Double parSpreadPv01Offer;

	private Double quoteSpreadPv01Bid;

	private Double quoteSpreadPv01Mid;

	private Double quoteSpreadPv01Offer;

	private Double hazardRate;

	private Double cumProbabilityOfDefault;

	private String cmaShortTermImpliedRating;

	private String cmaLongTermImpliedRating;

	private String spRating;

	private String moodysRating;

	private String delta;

	private String reference;

	private Integer marketRecoveryRate;

	private Integer isdaRecoveryRate;

	private String devaluationFactor;

	private String observedDerivedIndicator;

	private String derivationType;

	private String refCurveInfo;

	private Date lastContributionDateTime;
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getTenor() {
		return tenor;
	}

	public void setTenor(Double tenor) {
		this.tenor = tenor;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getBusinessDateTime() {
		return businessDateTime;
	}

	public void setBusinessDateTime(Date businessDateTime) {
		this.businessDateTime = businessDateTime;
	}

	public String getIceCdsCode() {
		return iceCdsCode;
	}

	public void setIceCdsCode(String iceCdsCode) {
		this.iceCdsCode = iceCdsCode;
	}

	public String getBbgCdsTicker() {
		return bbgCdsTicker;
	}

	public void setBbgCdsTicker(String bbgCdsTicker) {
		this.bbgCdsTicker = bbgCdsTicker;
	}

	public Integer getCmaEntityId() {
		return cmaEntityId;
	}

	public void setCmaEntityId(Integer cmaEntityId) {
		this.cmaEntityId = cmaEntityId;
	}

	public String getCmaTicker() {
		return cmaTicker;
	}

	public void setCmaTicker(String cmaTicker) {
		this.cmaTicker = cmaTicker;
	}

	public String getContractStandard() {
		return contractStandard;
	}

	public void setContractStandard(String contractStandard) {
		this.contractStandard = contractStandard;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSeniority() {
		return seniority;
	}

	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}

	public String getRestructuringType() {
		return restructuringType;
	}

	public void setRestructuringType(String restructuringType) {
		this.restructuringType = restructuringType;
	}

	public Integer getCoupon() {
		return coupon;
	}

	public void setCoupon(Integer coupon) {
		this.coupon = coupon;
	}

	public String getMarketQuotingConvention() {
		return marketQuotingConvention;
	}

	public void setMarketQuotingConvention(String marketQuotingConvention) {
		this.marketQuotingConvention = marketQuotingConvention;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public Date getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}

	public Date getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Date auctionDate) {
		this.auctionDate = auctionDate;
	}

	public Double getParSpreadBid() {
		return parSpreadBid;
	}

	public void setParSpreadBid(Double parSpreadBid) {
		this.parSpreadBid = parSpreadBid;
	}

	public Double getParSpreadMid() {
		return parSpreadMid;
	}

	public void setParSpreadMid(Double parSpreadMid) {
		this.parSpreadMid = parSpreadMid;
	}

	public Double getParSpreadOffer() {
		return parSpreadOffer;
	}

	public void setParSpreadOffer(Double parSpreadOffer) {
		this.parSpreadOffer = parSpreadOffer;
	}

	public Double getQuoteSpreadBid() {
		return quoteSpreadBid;
	}

	public void setQuoteSpreadBid(Double quoteSpreadBid) {
		this.quoteSpreadBid = quoteSpreadBid;
	}

	public Double getQuoteSpreadMid() {
		return quoteSpreadMid;
	}

	public void setQuoteSpreadMid(Double quoteSpreadMid) {
		this.quoteSpreadMid = quoteSpreadMid;
	}

	public Double getQuoteSpreadOffer() {
		return quoteSpreadOffer;
	}

	public void setQuoteSpreadOffer(Double quoteSpreadOffer) {
		this.quoteSpreadOffer = quoteSpreadOffer;
	}

	public Double getUpFrontBid() {
		return upFrontBid;
	}

	public void setUpFrontBid(Double upFrontBid) {
		this.upFrontBid = upFrontBid;
	}

	public Double getUpFrontMid() {
		return upFrontMid;
	}

	public void setUpFrontMid(Double upFrontMid) {
		this.upFrontMid = upFrontMid;
	}

	public Double getUpFrontOffer() {
		return upFrontOffer;
	}

	public void setUpFrontOffer(Double upFrontOffer) {
		this.upFrontOffer = upFrontOffer;
	}

	public Double getPercentOfParBid() {
		return percentOfParBid;
	}

	public void setPercentOfParBid(Double percentOfParBid) {
		this.percentOfParBid = percentOfParBid;
	}

	public Double getPercentOfParMid() {
		return percentOfParMid;
	}

	public void setPercentOfParMid(Double percentOfParMid) {
		this.percentOfParMid = percentOfParMid;
	}

	public Double getPercentOfParOffer() {
		return percentOfParOffer;
	}

	public void setPercentOfParOffer(Double percentOfParOffer) {
		this.percentOfParOffer = percentOfParOffer;
	}

	public Double getParSpreadPv01Bid() {
		return parSpreadPv01Bid;
	}

	public void setParSpreadPv01Bid(Double parSpreadPv01Bid) {
		this.parSpreadPv01Bid = parSpreadPv01Bid;
	}

	public Double getParSpreadPv01Mid() {
		return parSpreadPv01Mid;
	}

	public void setParSpreadPv01Mid(Double parSpreadPv01Mid) {
		this.parSpreadPv01Mid = parSpreadPv01Mid;
	}

	public Double getParSpreadPv01Offer() {
		return parSpreadPv01Offer;
	}

	public void setParSpreadPv01Offer(Double parSpreadPv01Offer) {
		this.parSpreadPv01Offer = parSpreadPv01Offer;
	}

	public Double getQuoteSpreadPv01Bid() {
		return quoteSpreadPv01Bid;
	}

	public void setQuoteSpreadPv01Bid(Double quoteSpreadPv01Bid) {
		this.quoteSpreadPv01Bid = quoteSpreadPv01Bid;
	}

	public Double getQuoteSpreadPv01Mid() {
		return quoteSpreadPv01Mid;
	}

	public void setQuoteSpreadPv01Mid(Double quoteSpreadPv01Mid) {
		this.quoteSpreadPv01Mid = quoteSpreadPv01Mid;
	}

	public Double getQuoteSpreadPv01Offer() {
		return quoteSpreadPv01Offer;
	}

	public void setQuoteSpreadPv01Offer(Double quoteSpreadPv01Offer) {
		this.quoteSpreadPv01Offer = quoteSpreadPv01Offer;
	}

	public Double getHazardRate() {
		return hazardRate;
	}

	public void setHazardRate(Double hazardRate) {
		this.hazardRate = hazardRate;
	}

	public Double getCumProbabilityOfDefault() {
		return cumProbabilityOfDefault;
	}

	public void setCumProbabilityOfDefault(Double cumProbabilityOfDefault) {
		this.cumProbabilityOfDefault = cumProbabilityOfDefault;
	}

	public String getCmaShortTermImpliedRating() {
		return cmaShortTermImpliedRating;
	}

	public void setCmaShortTermImpliedRating(String cmaShortTermImpliedRating) {
		this.cmaShortTermImpliedRating = cmaShortTermImpliedRating;
	}

	public String getCmaLongTermImpliedRating() {
		return cmaLongTermImpliedRating;
	}

	public void setCmaLongTermImpliedRating(String cmaLongTermImpliedRating) {
		this.cmaLongTermImpliedRating = cmaLongTermImpliedRating;
	}

	public String getSpRating() {
		return spRating;
	}

	public void setSpRating(String spRating) {
		this.spRating = spRating;
	}

	public String getMoodysRating() {
		return moodysRating;
	}

	public void setMoodysRating(String moodysRating) {
		this.moodysRating = moodysRating;
	}

	public String getDelta() {
		return delta;
	}

	public void setDelta(String delta) {
		this.delta = delta;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Integer getMarketRecoveryRate() {
		return marketRecoveryRate;
	}

	public void setMarketRecoveryRate(Integer marketRecoveryRate) {
		this.marketRecoveryRate = marketRecoveryRate;
	}

	public Integer getIsdaRecoveryRate() {
		return isdaRecoveryRate;
	}

	public void setIsdaRecoveryRate(Integer isdaRecoveryRate) {
		this.isdaRecoveryRate = isdaRecoveryRate;
	}

	public String getDevaluationFactor() {
		return devaluationFactor;
	}

	public void setDevaluationFactor(String devaluationFactor) {
		this.devaluationFactor = devaluationFactor;
	}

	public String getObservedDerivedIndicator() {
		return observedDerivedIndicator;
	}

	public void setObservedDerivedIndicator(String observedDerivedIndicator) {
		this.observedDerivedIndicator = observedDerivedIndicator;
	}

	public String getDerivationType() {
		return derivationType;
	}

	public void setDerivationType(String derivationType) {
		this.derivationType = derivationType;
	}

	public String getRefCurveInfo() {
		return refCurveInfo;
	}

	public void setRefCurveInfo(String refCurveInfo) {
		this.refCurveInfo = refCurveInfo;
	}

	public Date getLastContributionDateTime() {
		return lastContributionDateTime;
	}

	public void setLastContributionDateTime(Date lastContributionDateTime) {
		this.lastContributionDateTime = lastContributionDateTime;
	}

	@Override
	public String toString() {
		return "CDSDataDTO [entityName=" + entityName + ", currency=" + currency + ", tenor=" + tenor + ", identifier="
				+ identifier + ", maturityDate=" + maturityDate + ", businessDateTime=" + businessDateTime
				+ ", iceCdsCode=" + iceCdsCode + ", bbgCdsTicker=" + bbgCdsTicker + ", cmaEntityId=" + cmaEntityId
				+ ", cmaTicker=" + cmaTicker + ", contractStandard=" + contractStandard + ", sector=" + sector
				+ ", region=" + region + ", seniority=" + seniority + ", restructuringType=" + restructuringType
				+ ", coupon=" + coupon + ", marketQuotingConvention=" + marketQuotingConvention + ", instrumentType="
				+ instrumentType + ", defaultDate=" + defaultDate + ", auctionDate=" + auctionDate + ", parSpreadBid="
				+ parSpreadBid + ", parSpreadMid=" + parSpreadMid + ", parSpreadOffer=" + parSpreadOffer
				+ ", quoteSpreadBid=" + quoteSpreadBid + ", quoteSpreadMid=" + quoteSpreadMid + ", quoteSpreadOffer="
				+ quoteSpreadOffer + ", upFrontBid=" + upFrontBid + ", upFrontMid=" + upFrontMid + ", upFrontOffer="
				+ upFrontOffer + ", percentOfParBid=" + percentOfParBid + ", percentOfParMid=" + percentOfParMid
				+ ", percentOfParOffer=" + percentOfParOffer + ", parSpreadPv01Bid=" + parSpreadPv01Bid
				+ ", parSpreadPv01Mid=" + parSpreadPv01Mid + ", parSpreadPv01Offer=" + parSpreadPv01Offer
				+ ", quoteSpreadPv01Bid=" + quoteSpreadPv01Bid + ", quoteSpreadPv01Mid=" + quoteSpreadPv01Mid
				+ ", quoteSpreadPv01Offer=" + quoteSpreadPv01Offer + ", hazardRate=" + hazardRate
				+ ", cumProbabilityOfDefault=" + cumProbabilityOfDefault + ", cmaShortTermImpliedRating="
				+ cmaShortTermImpliedRating + ", cmaLongTermImpliedRating=" + cmaLongTermImpliedRating + ", spRating="
				+ spRating + ", moodysRating=" + moodysRating + ", delta=" + delta + ", reference=" + reference
				+ ", marketRecoveryRate=" + marketRecoveryRate + ", isdaRecoveryRate=" + isdaRecoveryRate
				+ ", devaluationFactor=" + devaluationFactor + ", observedDerivedIndicator=" + observedDerivedIndicator
				+ ", derivationType=" + derivationType + ", refCurveInfo=" + refCurveInfo
				+ ", lastContributionDateTime=" + lastContributionDateTime + "]";
	}
}

