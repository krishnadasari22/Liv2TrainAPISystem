package com.televisory.bond.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cds_data")
public class CDSData {

	@EmbeddedId
	CDSDataDetails cdsDataDetails;
	
	@Column(name = "cds_identifier")
	private String identifier;
	
	@Column(name = "maturity_date")
	private Date maturityDate;

	@Column(name = "ice_cds_code")
	private String iceCdsCode;

	@Column(name = "bbg_cds_ticker")
	private String bbgCdsTicker;

	@Column(name = "cma_entity_id")
	private Integer cmaEntityId;

	@Column(name = "cma_ticker")
	private String cmaTicker;

	@Column(name = "contract_standard")
	private String contractStandard;

	@Column(name = "sector")
	private String sector;

	@Column(name = "region")
	private String region;

	@Column(name = "coupon")
	private Integer coupon;

	@Column(name = "market_quoting_convention")
	private String marketQuotingConvention;

	@Column(name = "instrument_type")
	private String instrumentType;

	@Column(name = "default_date")
	private Date defaultDate;

	@Column(name = "auction_date")
	private Date auctionDate;

	@Column(name = "par_spread_bid")
	private Double parSpreadBid;

	@Column(name = "par_spread_mid")
	private Double parSpreadMid;

	@Column(name = "par_spread_offer")
	private Double parSpreadOffer;

	@Column(name = "quote_spread_bid")
	private Double quoteSpreadBid;

	@Column(name = "quote_spread_mid")
	private Double quoteSpreadMid;

	@Column(name = "quote_spread_offer")
	private Double quoteSpreadOffer;

	@Column(name = "up_front_bid")
	private Double upFrontBid;

	@Column(name = "up_front_mid")
	private Double upFrontMid;

	@Column(name = "up_front_offer")
	private Double upFrontOffer;

	@Column(name = "percent_of_par_bid")
	private Double percentOfParBid;

	@Column(name = "percent_of_par_mid")
	private Double percentOfParMid;

	@Column(name = "percent_of_par_offer")
	private Double percentOfParOffer;

	@Column(name = "par_spread_pv01_bid")
	private Double parSpreadPv01Bid;

	@Column(name = "par_spread_pv01_mid")
	private Double parSpreadPv01Mid;

	@Column(name = "par_spread_pv01_offer")
	private Double parSpreadPv01Offer;

	@Column(name = "quote_spread_pv01_bid")
	private Double quoteSpreadPv01Bid;

	@Column(name = "quote_spread_pv01_mid")
	private Double quoteSpreadPv01Mid;

	@Column(name = "quote_spread_pv01_offer")
	private Double quoteSpreadPv01Offer;

	@Column(name = "hazard_rate")
	private Double hazardRate;

	@Column(name = "cum_probability_of_default")
	private Double cumProbabilityOfDefault;

	@Column(name = "cma_short_term_implied_rating")
	private String cmaShortTermImpliedRating;

	@Column(name = "cma_long_term_implied_rating")
	private String cmaLongTermImpliedRating;

	@Column(name = "sp_rating")
	private String spRating;

	@Column(name = "moodys_rating")
	private String moodysRating;

	@Column(name = "delta")
	private String delta;

	@Column(name = "reference")
	private String reference;

	@Column(name = "market_recovery_rate")
	private Integer marketRecoveryRate;

	@Column(name = "isda_recovery_rate")
	private Integer isdaRecoveryRate;

	@Column(name = "devaluation_factor")
	private String devaluationFactor;

	@Column(name = "observed_derived_indicator")
	private String observedDerivedIndicator;

	@Column(name = "derivation_type")
	private String derivationType;

	@Column(name = "ref_curve_info")
	private String refCurveInfo;

	@Column(name = "last_contribution_date_time")
	private Date lastContributionDateTime;

	public CDSDataDetails getCdsDataDetails() {
		return cdsDataDetails;
	}

	public void setCdsDataDetails(CDSDataDetails cdsDataDetails) {
		this.cdsDataDetails = cdsDataDetails;
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
		return "CDSData [cdsDataDetails=" + cdsDataDetails + ", identifier=" + identifier + ", maturityDate="
				+ maturityDate + ", iceCdsCode=" + iceCdsCode + ", bbgCdsTicker=" + bbgCdsTicker + ", cmaEntityId="
				+ cmaEntityId + ", cmaTicker=" + cmaTicker + ", contractStandard=" + contractStandard + ", sector="
				+ sector + ", region=" + region + ", coupon=" + coupon + ", marketQuotingConvention="
				+ marketQuotingConvention + ", instrumentType=" + instrumentType + ", defaultDate=" + defaultDate
				+ ", auctionDate=" + auctionDate + ", parSpreadBid=" + parSpreadBid + ", parSpreadMid=" + parSpreadMid
				+ ", parSpreadOffer=" + parSpreadOffer + ", quoteSpreadBid=" + quoteSpreadBid + ", quoteSpreadMid="
				+ quoteSpreadMid + ", quoteSpreadOffer=" + quoteSpreadOffer + ", upFrontBid=" + upFrontBid
				+ ", upFrontMid=" + upFrontMid + ", upFrontOffer=" + upFrontOffer + ", percentOfParBid="
				+ percentOfParBid + ", percentOfParMid=" + percentOfParMid + ", percentOfParOffer=" + percentOfParOffer
				+ ", parSpreadPv01Bid=" + parSpreadPv01Bid + ", parSpreadPv01Mid=" + parSpreadPv01Mid
				+ ", parSpreadPv01Offer=" + parSpreadPv01Offer + ", quoteSpreadPv01Bid=" + quoteSpreadPv01Bid
				+ ", quoteSpreadPv01Mid=" + quoteSpreadPv01Mid + ", quoteSpreadPv01Offer=" + quoteSpreadPv01Offer
				+ ", hazardRate=" + hazardRate + ", cumProbabilityOfDefault=" + cumProbabilityOfDefault
				+ ", cmaShortTermImpliedRating=" + cmaShortTermImpliedRating + ", cmaLongTermImpliedRating="
				+ cmaLongTermImpliedRating + ", spRating=" + spRating + ", moodysRating=" + moodysRating + ", delta="
				+ delta + ", reference=" + reference + ", marketRecoveryRate=" + marketRecoveryRate
				+ ", isdaRecoveryRate=" + isdaRecoveryRate + ", devaluationFactor=" + devaluationFactor
				+ ", observedDerivedIndicator=" + observedDerivedIndicator + ", derivationType=" + derivationType
				+ ", refCurveInfo=" + refCurveInfo + ", lastContributionDateTime=" + lastContributionDateTime + "]";
	}
}
