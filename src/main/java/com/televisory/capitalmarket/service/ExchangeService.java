package com.televisory.capitalmarket.service;
import org.springframework.stereotype.Service;

import com.televisory.capitalmarket.util.CMStatic;

@Service
public class ExchangeService {
	
	/*Logger _log = Logger.getLogger(ExchangeService.class);
	
	@Autowired
	CMRepository exchangeRepository;
	
	@Autowired
	CMFinancialDataRepository factSetRepository;
	
	@Autowired
	DateUtil dateUtil;
	
	@Autowired
	CapitalMarketService capitalMarketService;
	
	@Autowired
	BetaCalculationService betaCalculationService;*/

	/*public List<CompanyDTO> findAllExchangeCompanies(String exchangeName) {
		
		List<CompanyDTO> companyList =new ArrayList<>();
		
		if(exchangeName.equals("FACTSET"))
			companyList=factSetRepository.getFactSetExchangeCompanies(exchangeName);
		else
			companyList=exchangeRepository.findAllExchangeCompanies(exchangeName);
		return companyList;
	}
	
	public List<CompanyDTO> findAllExchangeCompanies(String dbName,String searchCriteria,Integer countryId) {
	
		List<CompanyDTO> companyList=exchangeRepository.getCMExchangeCompanies(dbName,searchCriteria,countryId);	
		return companyList;
	}

	public List<IndexDTO> findExchangeIndex(String exchangeName) {
		List<IndexDTO> bseIndex =exchangeRepository.findExchangeIndex(exchangeName);
		return bseIndex;
	}

	public List<CompanyDTO> findIndexCompany(Integer indexId,String exchangeName) {
		List<CompanyDTO> companyList= exchangeRepository.findIndexCompany(indexId,exchangeName);
		return companyList;
	}

	public IndexDTO getIndexbyId(Integer indexId,String exchangeName) {
		return exchangeRepository.getIndexbyId(indexId,exchangeName);
	}

	public CompanyDTO getExchangeCompanyByCode(String code,String exchangeName) {
		return exchangeRepository.getExchangeCompanyByCode(code,exchangeName);
	}
	
	
	public <T> List<StockPriceDTO> getStockPrice(Integer Id, Date startDate, Date endDate, String periodType,
			Class<T> type,String exchangeName) {
		List<StockPriceDTO> exchangeStockPrice  = exchangeRepository.getStockPrice(Id, startDate, endDate, type,exchangeName);
		Integer period = null;
		try {
			if (periodType.equals(CMStatic.PERIODICITY_WEEKLY)) {
				period = 7;
				exchangeStockPrice = capitalMarketService.getStockPriceBasedOnPeriodicity(exchangeStockPrice, period);
			} else if (periodType.equals(CMStatic.DAILY)) {
				period=1;
			}
			exchangeStockPrice = capitalMarketService.getStockChangeRate(exchangeStockPrice, periodType, period);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exchangeStockPrice;
	}*/
	
	/*public <T> StockPriceDTO getStockPriceHighest(Integer companyId, Date startDate, Date endDate,
			Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceHighest(companyId, startDate, endDate, type,exchangeName);
	}
	
	public <T> StockPriceDTO getStockPriceHighest(Integer companyId, Integer days, Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceHighest(companyId, days, type,exchangeName);
	}
	
	public <T> StockPriceDTO getStockPriceHighest(Integer companyId, Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceHighest(companyId, type,exchangeName);
	}
	
	public <T> StockPriceDTO getStockPriceLowest(Integer companyId, Date startDate, Date endDate,
			Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceLowest(companyId, startDate, endDate, type,exchangeName);
	}
	
	public <T> StockPriceDTO getStockPriceLowest(Integer companyId, Integer days, Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceLowest(companyId, days, type,exchangeName);
	}
	
	public <T> StockPriceDTO getStockPriceLowest(Integer companyId, Class<T> type,String exchangeName) {
		return exchangeRepository.getStockPriceLowest(companyId, type,exchangeName);
	}
	
	public List<CompanyFinancialDTO> getCompanyPnl(Integer companyId,String exchangeName) {
		return exchangeRepository.getCompanyPnl(companyId,exchangeName);
	}

	public List<CompanyFinancialDTO> getCompanyPnlByPeriod(Integer companyId, String periodType, Date startDate, Date endDate,String exchangeName) {
		return exchangeRepository.getCompanyPnlByPeriod(companyId,periodType,startDate,endDate,exchangeName);
	}

	public List<String> getFinancialPeriodicity(Integer companyId,String exchangeName) {
		return exchangeRepository.getFinancialPeriodicity(companyId,exchangeName);
	}

	public List<IndexDTO> getCompanyIndexes(Integer companyId, String exchangeName) {
		return exchangeRepository.getCompanyIndexes(companyId,exchangeName);
	}
	
	public  BetaData getCompanyBeta( Integer companyId, String periodicity, Integer indexId) throws Exception {
		
		_log.info("getting beta data");
		
		IndexDTO bseIndexe = exchangeRepository.getIndexbyId(indexId,"FACTSET");
		List<StockPriceDTO> companyStockPrice = exchangeRepository.getStockPrice(companyId,null,null,ExchangeCompanyStockPrice.class,"FACTSET");
		List<StockPriceDTO> indexStockPrices = exchangeRepository.getStockPrice(indexId,null,null,ExchangeIndexStockPrice.class,"FACTSET");
			
		return betaCalculationService.getBetaData(companyStockPrice, indexStockPrices, bseIndexe, periodicity);
	}
	
	public  List<BetaData> getCompanyBeta(Integer companyId, String periodicity, List<String> filterList,String exchangeName) throws Exception {
		
		_log.info("getting beta data");
		List<StockPriceDTO> companyStockPrice = exchangeRepository.getStockPrice(companyId,null,null,ExchangeCompanyStockPrice.class,exchangeName);
		
		List< List<StockPriceDTO> > indexStockPrices = new ArrayList<List<StockPriceDTO>>();
		List<IndexDTO> bseIndexes=new ArrayList<>();
		
		for (Iterator<String> iterator = filterList.iterator(); iterator.hasNext();) {
			Integer indexId=Integer.parseInt(iterator.next());
			 bseIndexes.add(exchangeRepository.getIndexbyId(indexId,exchangeName));
			 indexStockPrices.add(exchangeRepository.getStockPrice(indexId,null,null,ExchangeIndexStockPrice.class,exchangeName));
		}	
		return betaCalculationService.getBetaData(companyStockPrice,indexStockPrices,bseIndexes,periodicity);
	}

	public List<StockPriceWithCompanyDTO> getTopGainerOrLoser(String exchangeName,String performanceType, Integer indexId, Integer resultLimit) {
		return exchangeRepository.getTopGainerOrLoser(exchangeName,performanceType,indexId,resultLimit);
	}

	public List<StockPriceWithCompanyDTO> getExchangePerformers(String exchangeName) {
		return exchangeRepository.getExchangePerformers(exchangeName);
	}

	public List<CompanyFinancialDTO> getCompanyBalanceSheet(Integer companyId,String exchangeName) {
		return exchangeRepository.getCompanyBalanceSheet(companyId,exchangeName);
	}
	
	public  List<CompanyFinancialDTO> getCompanyBalanceSheetByPeriod(Integer companyId,String periodType,Date startDate,Date endDate,String exchangeName) {
		return exchangeRepository.getCompanyBalanceSheetByPeriod(companyId,periodType,startDate,endDate,exchangeName);
	}

	public List<CompanyFinancialDTO> getCompanyCashFlow(Integer companyId,String exchangeName) {
		return exchangeRepository.getCompanyCashFlow(companyId,exchangeName);
	}

	public  List<CompanyFinancialDTO> getCompanyCashFlowByPeriod(Integer companyId,String periodType,Date startDate,Date endDate,String exchangeName) {
		return exchangeRepository.getCompanyCashFlowByPeriod(companyId,periodType,startDate,endDate,exchangeName);
	}*/
	
}
