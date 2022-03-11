package com.televisory.utils;

public interface PEVCFundingDetailAdvancedSerchedQueries {

    /******************************************************
     * PEVC funding detail table queries starts here
     ***********************************************/

    String pevcFundDetailFinTypeVC = "SELECT (@id\\:=@id + 1) AS id, M.* FROM (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,inception_date AS inception_date,portco_fin_type, sum(valuation) / 1 AS valuation,"
            + "sum(valuation_cal) / 1 AS valuation_cal, :currency as target_currency,count(inception_date) as count,'Million' as unit,company_id,entity_type "
            + "FROM(SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,f.tics_industry_name,a.category_name_desc,a.inception_date AS inception_date,"
            + "a.portco_fin_type,a.valuation  AS valuation,(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) AS valuation_cal,:currency as target_currency, company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE a.inception_date BETWEEN :startDate AND :endDate " + "#country_industry_entity_filter_template "
            + "AND  a.portco_fin_type = 'vc' and a.iso_currency is not null "
            +"#issue-filter "
            + "ORDER BY a.inception_date desc )x group by factset_entity_id having valuation_cal BETWEEN :minAmount AND :maxAmount  or valuation_cal is null ORDER BY #sorting_template )M  JOIN (SELECT @id\\:=:rowOffset) AS ai LIMIT :rowOffset,:rowCount";

    String pevcFundDetailFinTypeVCCount = "SELECT" +
            " COUNT(*) " +
            " FROM " +
            " (SELECT " +
            " factset_entity_id," +
            " SUM(valuation) / 1 AS valuation," +
            " SUM(valuation_cal) / 1 AS valuation_cal," +
            "'USD' AS target_currency," +
            " 'Million' AS unit " +
            " FROM " +
            " (SELECT " +
            " b.factset_entity_id," +
            " a.valuation AS valuation," +
            " (a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) AS valuation_cal," +
            " 'USD' AS target_currency," +
            " 'Million' AS unit" +
            "  FROM " +
            " factset.pe_v1_pe_securities a " +
            " INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " +
            " JOIN (SELECT " +
            " country_iso_code_2, country_iso_code_3, country_name " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country " +
            " JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id " +
            " JOIN (SELECT " +
            " tics_industry_code, ff_ind_code " +
            " FROM " +
            " cm.tics_industry_mapping " +
            " GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " +
            " JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " +
            " WHERE " +
            " a.inception_date BETWEEN :startDate AND :endDate " + "#country_industry_entity_filter_template "+
            "AND a.portco_fin_type = 'vc' "+
            "AND a.iso_currency IS NOT NULL " +
            "#issue-filter " +
            " ORDER BY a.inception_date DESC) x " +
            " GROUP BY factset_entity_id) M " +
            " where " +
            " valuation_cal BETWEEN :minAmount AND :maxAmount or  valuation_cal is null " ;


    String pevcFundDetailFinTypePE = "select (@id\\:=@id + 1) AS id, M.* FROM (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,"
            + "category_name_desc,inception_date,portco_fin_type,sum(valuation) AS valuation,sum(valuation_cal) AS valuation_cal,:currency as target_currency,count(inception_date) as count,'Million' as unit,company_id,entity_type "
            + "FROM (SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3 ,c.country_name,f.tics_industry_code,f.tics_industry_name,a.category_name_desc,"
            + "g.event_date AS inception_date,a.portco_fin_type,h.transaction_value AS valuation,(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_cal,:currency as target_currency,company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "JOIN factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "LEFT JOIN (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template " + "AND a.portco_fin_type = 'PE' "
            + "AND h.ver = '1' and a.iso_currency is not null "
            +"#issue-filter "
            + "AND g.deal_id is not null ORDER BY a.inception_date desc )x  GROUP BY entity_proper_name having valuation_cal BETWEEN :minAmount AND :maxAmount or valuation_cal is null  ORDER BY #sorting_template ) M  JOIN (SELECT @id\\:=:rowOffset) AS ai limit :rowOffset,:rowCount";

    String pevcFundDetailFinTypePECount = "SELECT COUNT(*) FROM (SELECT entity_proper_name,sum(valuation_cal) AS valuation_cal "
            + "FROM (SELECT b.entity_proper_name,(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_cal " + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template " + "AND a.portco_fin_type = 'PE'"+"#issue-filter "
            + "AND h.ver = '1' and a.iso_currency is not null AND g.deal_id is not null ORDER BY inception_date desc )x GROUP BY entity_proper_name ) M where  valuation_cal between :minAmount and :maxAmount or valuation_cal is null ";

    String pevcFundDetailFinTypeOT = "select (@id\\:=@id + 1) AS id, M.* from (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,"
            + "tics_industry_code,tics_industry_name,category_name_desc,inception_date,portco_fin_type,"
            + "sum(valuation) AS valuation,sum(valuation_cal) AS valuation_cal,:currency as target_currency,count(inception_date) as count,'Million' as unit,company_id,entity_type "
            + "FROM(SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3 ,c.country_name,"
            + "f.tics_industry_code,f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,a.portco_fin_type,h.transaction_value AS valuation,"
            + "(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_cal,:currency as target_currency,company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE  g.event_date between :startDate AND :endDate  AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template " + "AND a.portco_fin_type = 'OT' "
            + "AND h.ver = '1' and a.iso_currency is not null "
            +"#issue-filter "
            + "AND g.deal_id is not null ORDER BY inception_date desc ) x  GROUP BY entity_proper_name having valuation_cal BETWEEN :minAmount AND :maxAmount or valuation_cal is null ORDER BY #sorting_template )M  JOIN (SELECT @id\\:=:rowOffset) AS ai limit :rowOffset,:rowCount";

    String pevcFundDetailFinTypeOTCount = "select COUNT(*) from (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,"
            + "tics_industry_code,tics_industry_name,category_name_desc,inception_date,portco_fin_type,sum(valuation) AS valuation,"
            + "count(inception_date) as count,'Million' as unit "
            // + ",company_id,entity_type "
            + "FROM(SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3 ,c.country_name,"
            + "f.tics_industry_code,f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,a.portco_fin_type,h.transaction_value AS valuation "
            + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template AND a.portco_fin_type = 'OT' "+"#issue-filter "
            + "AND h.ver = '1' and a.iso_currency is not null "
            + "AND g.deal_id is not null ORDER BY inception_date desc )x  GROUP BY entity_proper_name ) M where valuation between :minAmount and :maxAmount or valuation is null ";

    String pevcFundDetailFinTypeNotSelected = "SELECT (@id\\:=@id + 1) AS id, M.* FROM (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,inception_date,GROUP_CONCAT(portco_fin_type) AS portco_fin_type,sum(valuation) as valuation,sum(valuation_cal) as valuation_cal,target_currency,'Million' AS unit,SUM(count) AS count,company_id,entity_type "
            + "FROM ((SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation)/ 1  AS valuation,SUM((valuation_cal)) / 1 AS valuation_cal,target_currency,COUNT(inception_date) as count,company_id,entity_type "
            + "FROM( select b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,f.tics_industry_name,"
            + "a.category_name_desc,a.inception_date,a.portco_fin_type,a.valuation ,"
            + "(a.valuation) * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date) as valuation_cal,:currency AS target_currency,company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name " + "FROM cm.country_list "
            + "GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE a.inception_date BETWEEN :startDate AND :endDate " + "#country_industry_entity_filter_template "
            + "AND a.portco_fin_type = 'vc' AND a.iso_currency IS NOT NULL  #issue-filter order by a.inception_date desc)x "
            + "GROUP BY entity_proper_name) UNION (SELECT "
            + "factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation) AS valuation,SUM(valuation_cal) AS valuation_cal,"
            + "target_currency,COUNT(inception_date) as count,company_id,entity_type from (select  b.factset_entity_id,"
            + "b.entity_proper_name,c.country_iso_code_3,c.country_name,"
            + "f.tics_industry_code,f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,"
            + "a.portco_fin_type,h.transaction_value AS valuation,h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date) AS valuation_cal,"
            + ":currency AS target_currency,company_id,entity_type_desc as entity_type  FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name " + "FROM cm.country_list "
            + "GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id  "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE g.event_date BETWEEN :startDate AND :endDate and (a.inception_date BETWEEN :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template "
            + "AND a.portco_fin_type != 'vc' AND h.ver = '1' AND a.iso_currency IS NOT NULL AND g.deal_id is not null #issue-filter order by a.inception_date desc)x "
            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name having "
            +" valuation_cal BETWEEN :minAmount AND :maxAmount or valuation_cal is null "
            + " ORDER BY #sorting_template ) M JOIN (SELECT @id\\:=:rowOffset) AS ai LIMIT :rowOffset,:rowCount";

    String pevcFundDetailFinTypeNotSelectedCount = "SELECT COUNT(*) FROM (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,inception_date,GROUP_CONCAT(portco_fin_type) AS portco_fin_type,sum(valuation) as valuation,"
            + "'Million' AS unit,SUM(count) AS count,issue_type "
            + "FROM ((SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation)/ 1  AS valuation," + "COUNT(inception_date) as count,issue_type "
            + "FROM( select b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,f.tics_industry_name,"
            + "a.category_name_desc,a.inception_date,a.portco_fin_type, (a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) AS valuation,issue_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name " + "FROM cm.country_list "
            + "GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "WHERE a.inception_date BETWEEN :startDate AND :endDate " + "#country_industry_entity_filter_template "
            + "AND a.portco_fin_type = 'vc' AND a.iso_currency IS NOT NULL #issue-filter order by a.inception_date desc)x "
            + "GROUP BY entity_proper_name) UNION (SELECT "
            + "factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation) AS valuation," + "COUNT(inception_date) as count,issue_type " + "from (select b.factset_entity_id,"
            + "b.entity_proper_name,c.country_iso_code_3,c.country_name,"
            + "f.tics_industry_code,f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,"
            + "a.portco_fin_type,h.transaction_value , (h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation,issue_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name " + "FROM cm.country_list "
            + "GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id  "
            + "WHERE g.event_date BETWEEN :startDate AND :endDate and (a.inception_date BETWEEN :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template "
            + "AND a.portco_fin_type != 'vc' #issue-filter  AND h.ver = '1' AND a.iso_currency IS NOT NULL AND g.deal_id is not null order by a.inception_date desc)x "
            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name) M where  valuation between :minAmount and :maxAmount or valuation is null ";

    String pevcFundDetailFinTypeVCPE = "select (@id\\:=@id + 1) AS id, M.* from (select * from (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
            + "tics_industry_name,category_name_desc,inception_date,GROUP_CONCAT(portco_fin_type) AS portco_fin_type,sum(valuation) as valuation,sum(valuation_cal) as valuation_cal,target_currency,'Million' AS unit,SUM(count) AS count,company_id,entity_type "
            + "FROM ((SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation)/ 1  AS valuation,SUM((valuation_cal)) / 1 AS valuation_cal,target_currency,COUNT(inception_date) as count,company_id,entity_type "
            + "FROM( select b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,"
            + "f.tics_industry_name,a.category_name_desc,a.inception_date,a.portco_fin_type,a.valuation,"
            + "(a.valuation) * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date) as valuation_cal,:currency AS target_currency,company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE a.inception_date between :startDate AND :endDate #country_industry_entity_filter_template "
            + "AND a.portco_fin_type = 'vc' and a.iso_currency is not null  #issue-filter  order by a.inception_date desc)x  GROUP BY entity_proper_name) "
            + "UNION (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
            + "SUM(valuation) AS valuation,SUM(valuation_cal) AS valuation_cal,target_currency,COUNT(inception_date) as count,company_id, entity_type "
            + "FROM (SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,"
            + "f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,a.portco_fin_type,h.transaction_value AS valuation,"
            + "h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date) AS valuation_cal,:currency AS target_currency,company_id,entity_type_desc as entity_type "
            + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template AND a.portco_fin_type = 'PE' "
            + "AND h.ver = '1'  AND a.iso_currency IS NOT NULL #issue-filter AND g.deal_id is not null order by a.inception_date desc)x  "
            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name)v "
            + "WHERE (portco_fin_type ='PE,VC' or portco_fin_type ='VC,PE') AND valuation_cal BETWEEN :minAmount AND :maxAmount or valuation_cal is null ORDER BY #sorting_template) M  JOIN (SELECT @id\\:=:rowOffset) AS ai limit :rowOffset,:rowCount";

    String pevcFundDetailFinTypeVCPECount = "select count(*) from (select * from (SELECT entity_proper_name,GROUP_CONCAT(portco_fin_type) AS fin_type,sum(valuation_cal) as valuation_cal "
            + "FROM ((SELECT entity_proper_name,portco_fin_type,(sum(valuation_cal) / 1) AS valuation_cal "
            + "FROM( select b.entity_proper_name,(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) AS valuation_cal, a.portco_fin_type " + "FROM factset.pe_v1_pe_securities a "
            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "WHERE a.inception_date between :startDate AND :endDate #country_industry_entity_filter_template "
            + "AND a.portco_fin_type = 'vc' and a.iso_currency is not null  #issue-filter order by a.inception_date desc)x GROUP BY entity_proper_name) "
            + "UNION (SELECT entity_proper_name,portco_fin_type,sum(valuation_cal) as valuation_cal "
            + "FROM (SELECT b.entity_proper_name,(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_cal,a.portco_fin_type " + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
            + "#country_industry_entity_filter_template AND a.portco_fin_type = 'PE' "
            + "AND h.ver = '1'  AND a.iso_currency IS NOT NULL #issue-filter AND g.deal_id is not null order by a.inception_date desc)x "
            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name)v "
            + "WHERE (fin_type ='PE,VC' or fin_type ='VC,PE')) M where  valuation_cal between :minAmount and :maxAmount or valuation_cal is null ";

    String pevcFundingInvestmentListQuery = "SELECT (@id\\:=@id + 1) AS id, z.* FROM (SELECT factset_portco_entity_id,entity_proper_name,security_name,category_name_desc,issue_type_desc as issue_type,portco_fin_type,inception_date,iso_currency,valuation,valuation_cal,target_currency,'Million' as unit "
            + "FROM ((SELECT s.factset_portco_entity_id," + "e.entity_proper_name," + "security_name,"
            + "category_name_desc,issue_type_desc,portco_fin_type,inception_date,iso_currency,"
            + "valuation / 1 as valuation,"
            + "(valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date)) / 1 AS valuation_cal,"
            + ":currency as target_currency FROM factset.pe_v1_pe_securities s "
            + "JOIN factset.sym_v1_sym_entity e ON s.factset_portco_entity_id = e.factset_entity_id "
            + "JOIN factset.ref_v2_issue_type_map m on m.issue_type_code=s.issue_type "
            + "WHERE factset_portco_entity_id = :entityId AND inception_date between :startDate AND :endDate AND portco_fin_type = 'vc') "
            + "UNION (SELECT a.factset_portco_entity_id, d.entity_proper_name, security_name,"
            + "category_name_desc, issue_type_desc, portco_fin_type, b.event_date as inception_date," + "iso_currency,"
            + "transaction_value  AS valuation,"
            + "(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, b.event_date)) AS valuation_cal,"
            + ":currency as target_currency " + "FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts b ON a.factset_portco_entity_id = b.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms c ON b.deal_id = c.deal_id "
            + "JOIN factset.sym_v1_sym_entity d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN factset.ref_v2_issue_type_map m on m.issue_type_code=a.issue_type "
            + "WHERE a.factset_portco_entity_id = :entityId AND b.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) AND portco_fin_type != 'vc' and c.ver=1)) x "
            + "WHERE portco_fin_type=:financingType ORDER BY inception_date DESC)z  JOIN (SELECT @id\\:=0) AS ai";

    String pevcFundingInvestmentListFinTypePEandVCQuery = "SELECT (@id\\:=@id + 1) AS id, z.* FROM (SELECT factset_portco_entity_id,entity_proper_name,security_name,category_name_desc,issue_type_desc as issue_type,portco_fin_type,inception_date,iso_currency,valuation,valuation_cal,target_currency,'Million' as unit "
            + "FROM ((SELECT s.factset_portco_entity_id, e.entity_proper_name, security_name,"
            + "category_name_desc, issue_type_desc, portco_fin_type, inception_date, iso_currency,"
            + "valuation / 1 as valuation,"
            + "(valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date)) / 1 AS valuation_cal,"
            + ":currency as target_currency FROM factset.pe_v1_pe_securities s "
            + "JOIN factset.sym_v1_sym_entity e ON s.factset_portco_entity_id = e.factset_entity_id "
            + "JOIN factset.ref_v2_issue_type_map m on m.issue_type_code=s.issue_type "
            + "WHERE factset_portco_entity_id = :entityId AND inception_date between :startDate AND :endDate AND portco_fin_type = 'vc') "
            + "UNION (SELECT a.factset_portco_entity_id," + "d.entity_proper_name," + "security_name,"
            + "category_name_desc, issue_type_desc, portco_fin_type, b.event_date as inception_date,"
            + "iso_currency, transaction_value  AS valuation,"
            + "(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, b.event_date)) AS valuation_cal,"
            + ":currency as target_currency FROM factset.pe_v1_pe_securities a "
            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts b ON a.factset_portco_entity_id = b.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms c ON b.deal_id = c.deal_id "
            + "JOIN factset.sym_v1_sym_entity d ON a.factset_portco_entity_id = d.factset_entity_id "
            + "JOIN factset.ref_v2_issue_type_map m on m.issue_type_code=a.issue_type "
            + "WHERE a.factset_portco_entity_id = :entityId AND b.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) AND portco_fin_type != 'vc' and c.ver=1)) x "
            + "ORDER BY inception_date DESC)z  JOIN (SELECT @id\\:=0) AS ai";

    /******************************************************
     * PEVC funding detail table queries ends here
     ***********************************************/

    String pevcTopFundedCompaniesBasedOnCountryQuery = "select entity_proper_name,factset_portco_entity_id,group_concat(distinct portco_fin_type) as portco_fin_type,sum(rounds) as rounds,max(inception_date) as "
            + "inception_date,sum(total_valuation) as total_valuation,sum(total_valuation_fx) as total_valuation_fx,country,tics_industry_name,tics_industry_code,:currency as currency,'Million' as unit,company_id ,"
            + "entity_type from ((SELECT e.entity_proper_name, factset_portco_entity_id, portco_fin_type, COUNT(*) as rounds, inception_date, SUM(valuation)/1 AS total_valuation, SUM(valuation * "
            + "factset.get_fx_daily_conversion(s.iso_currency,:currency,inception_date))/1 as total_valuation_fx, c.country_name as country,f.tics_industry_name,f.tics_industry_code,s.iso_currency,"
            + "cl.company_id,em.entity_type_desc as entity_type FROM factset.pe_v1_pe_securities s JOIN factset.sym_v1_sym_entity e ON e.factset_entity_id = s.factset_portco_entity_id JOIN (SELECT "
            + "country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = e.iso_country JOIN factset.sym_v1_sym_entity_sector d "
            + "ON s.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = d.sector_code JOIN (select tics_industry_code,ff_ind_code from "
            + "cm.tics_industry_mapping group by ff_ind_code) i ON d.industry_code = i.ff_ind_code JOIN cm.tics_industry f ON f.tics_industry_code = i.tics_industry_code left join (select * from "
            + "(select * from cm.company_list order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=s.factset_portco_entity_id join factset.ref_v2_entity_type_map em on "
            + "em.entity_type_code=e.entity_type WHERE inception_date BETWEEN :startDate AND :endDate AND portco_fin_type = 'VC' and c.country_iso_code_3=:countryIsoCode GROUP BY factset_portco_entity_id) "
            + "union (SELECT e.entity_proper_name, s.factset_portco_entity_id, portco_fin_type, COUNT(*) as rounds, evts.event_date as inception_date, SUM(transaction_value) AS total_valuation, "
            + "SUM(transaction_value * factset.get_fx_daily_conversion(s.iso_currency,:currency,evts.event_date)) as total_valuation_fx, c.country_name as country,f.tics_industry_name,f.tics_industry_code,"
            + "s.iso_currency,cl.company_id,em.entity_type_desc as entity_type FROM factset.pe_v1_pe_securities s JOIN factset.pe_v1_pe_portco_pvt_invest_evts evts ON evts.factset_portco_entity_id = "
            + "s.factset_portco_entity_id JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = evts.deal_id JOIN factset.sym_v1_sym_entity e ON e.factset_entity_id = s.factset_portco_entity_id JOIN "
            + "(SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = e.iso_country JOIN factset.sym_v1_sym_entity_sector d "
            + "ON s.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = d.sector_code JOIN (select tics_industry_code,ff_ind_code from "
            + "cm.tics_industry_mapping group by ff_ind_code)i ON d.industry_code = i.ff_ind_code JOIN cm.tics_industry f ON f.tics_industry_code = i.tics_industry_code left join (select * from "
            + "(select * from cm.company_list order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=s.factset_portco_entity_id join factset.ref_v2_entity_type_map em "
            + "on em.entity_type_code=e.entity_type WHERE evts.event_date BETWEEN :startDate AND :endDate AND (s.inception_date BETWEEN :startDate AND :endDate or s.inception_date is null) "
            + "AND portco_fin_type != 'VC' and c.country_iso_code_3=:countryIsoCode AND t.ver = 1 AND deal_id_code = 'MA' GROUP BY s.factset_portco_entity_id))x group by factset_portco_entity_id order by "
            + "total_valuation_fx desc limit ";

    String pevcTopFundedCompaniesBasedOnGlobalQuery = "select entity_proper_name,factset_portco_entity_id,group_concat(distinct portco_fin_type) as portco_fin_type,sum(rounds) as rounds,max(inception_date) as "
            + "inception_date,sum(total_valuation) as total_valuation,sum(total_valuation_fx) as total_valuation_fx,country,tics_industry_name,tics_industry_code,:currency as currency,'Million' as unit,company_id,"
            + "entity_type from ((SELECT e.entity_proper_name, factset_portco_entity_id, portco_fin_type, COUNT(*) as rounds, inception_date, SUM(valuation)/1 AS total_valuation, SUM(valuation * "
            + "factset.get_fx_daily_conversion(s.iso_currency,:currency,inception_date))/1 as total_valuation_fx,c.country_name as country,f.tics_industry_name,f.tics_industry_code,cl.company_id,"
            + "em.entity_type_desc as entity_type FROM factset.pe_v1_pe_securities s JOIN factset.sym_v1_sym_entity e ON e.factset_entity_id = s.factset_portco_entity_id JOIN (SELECT country_iso_code_2,"
            + "country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = e.iso_country JOIN factset.sym_v1_sym_entity_sector d ON s.factset_portco_entity_id = "
            + "d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = d.sector_code JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) "
            + "i ON d.industry_code = i.ff_ind_code JOIN cm.tics_industry f ON f.tics_industry_code = i.tics_industry_code left join (select * from (select * from cm.company_list order by domicile_flag desc )x "
            + "group by factset_entity_id) cl on cl.factset_entity_id=s.factset_portco_entity_id join factset.ref_v2_entity_type_map em on em.entity_type_code=e.entity_type WHERE inception_date "
            + "BETWEEN :startDate AND :endDate AND portco_fin_type = 'VC' GROUP BY factset_portco_entity_id) union (SELECT e.entity_proper_name, s.factset_portco_entity_id, portco_fin_type, COUNT(*) as "
            + "rounds, evts.event_date as inception_date, SUM(transaction_value)AS total_valuation, SUM(transaction_value * factset.get_fx_daily_conversion(s.iso_currency,:currency,evts.event_date))as "
            + "total_valuation_fx,c.country_name as country,f.tics_industry_name,f.tics_industry_code,cl.company_id,em.entity_type_desc as entity_type FROM factset.pe_v1_pe_securities s JOIN "
            + "factset.pe_v1_pe_portco_pvt_invest_evts evts ON evts.factset_portco_entity_id = s.factset_portco_entity_id JOIN factset.ma_v1_ma_deal_terms t ON t.deal_id = evts.deal_id JOIN "
            + "factset.sym_v1_sym_entity e ON e.factset_entity_id = s.factset_portco_entity_id JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) "
            + "c ON c.country_iso_code_2 = e.iso_country JOIN factset.sym_v1_sym_entity_sector d ON s.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON "
            + "m.factset_sector_code = d.sector_code JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) i ON d.industry_code = i.ff_ind_code JOIN cm.tics_industry f "
            + "ON f.tics_industry_code = i.tics_industry_code left join (select * from (select * from cm.company_list order by domicile_flag desc )x group by factset_entity_id) cl on "
            + "cl.factset_entity_id=s.factset_portco_entity_id join factset.ref_v2_entity_type_map em on em.entity_type_code=e.entity_type WHERE evts.event_date BETWEEN :startDate AND :endDate "
            + "AND (s.inception_date BETWEEN :startDate AND :endDate or s.inception_date is null) AND portco_fin_type != 'VC' AND t.ver = 1 AND deal_id_code = 'MA' GROUP BY s.factset_portco_entity_id))x "
            + "group by factset_portco_entity_id order by total_valuation_fx desc limit ";

    String pevcFundingDetailsByCountryGlobalQuery = "SELECT country_iso_code_3 as code, country_name as name, SUM(valuation) AS valuation, SUM(valuation_fx) "
            + "AS valuation_fx, :currency as target_currency,'Million' as unit FROM ((SELECT c.country_iso_code_3, c.country_name, "
            + "SUM(a.valuation) / 1 AS valuation, SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, "
            + "a.inception_date)) / 1 AS valuation_fx FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM "
            + "cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector "
            + "d ON a.factset_portco_entity_id = d.factset_entity_id WHERE a.inception_date BETWEEN :startDate AND :endDate AND "
            + "a.portco_fin_type = 'vc' GROUP BY country_name) UNION (SELECT c.country_iso_code_3, c.country_name, SUM(h.transaction_value) "
            + "AS valuation, SUM(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx "
            + "FROM factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id JOIN "
            + "(SELECT country_iso_code_2, country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON "
            + "c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = "
            + "d.factset_entity_id JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id WHERE g.event_date BETWEEN :startDate AND :endDate AND "
            + "a.inception_date BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' GROUP BY country_name)) x "
            + "GROUP BY country_name ORDER BY valuation_fx DESC";

    String pevcFundingDetailsBySectorGlobalQuery = "SELECT sector_code as code, sector as name, SUM(valuation) AS valuation, SUM(valuation_fx) AS valuation_fx, "
            + ":currency as target_currency, 'Million' as unit FROM ((SELECT d.sector_code, m.factset_sector_desc AS sector, "
            + "SUM(a.valuation) / 1 AS valuation, SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, "
            + "a.inception_date)) / 1 AS valuation_fx FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM "
            + "cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector "
            + "d ON a.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = "
            + "d.sector_code WHERE a.inception_date BETWEEN :startDate AND :endDate AND a.portco_fin_type = 'vc' GROUP BY "
            + "factset_sector_desc) UNION (SELECT d.sector_code, m.factset_sector_desc AS sector, SUM(h.transaction_value) AS valuation, "
            + "SUM(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx FROM "
            + "factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id JOIN "
            + "(SELECT country_iso_code_2, country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON "
            + "c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = "
            + "d.factset_entity_id JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code "
            + "= d.sector_code WHERE g.event_date BETWEEN :startDate AND :endDate AND a.inception_date BETWEEN :startDate AND "
            + ":endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' GROUP BY factset_sector_desc)) x GROUP BY sector ORDER BY "
            + "valuation_fx DESC;";

    String pevcFundingDetailsBySectorIndustryGlobalQuery = "SELECT tics_industry_code as code, industry as name, SUM(valuation) AS valuation, SUM(valuation_fx) "
            + "AS valuation_fx, :currency as target_currency,'Million' as unit,sector_code, sector FROM ((SELECT f.tics_industry_code, f.tics_industry_name AS "
            + "industry, SUM(a.valuation) / 1 AS valuation, SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, "
            + "a.inception_date)) / 1 AS valuation_fx ,d.sector_code, m.factset_sector_desc AS sector  FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b ON "
            + "a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM "
            + "cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector "
            + "d ON a.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = "
            + "d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON "
            + "f.tics_industry_code = e.tics_industry_code WHERE a.inception_date BETWEEN :startDate AND :endDate AND "
            + "a.portco_fin_type = 'vc' AND d.sector_code = :sectorCode AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code) "
            + "UNION (SELECT f.tics_industry_code, f.tics_industry_name AS industry, SUM(h.transaction_value) AS valuation, "
            + "SUM(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx, d.sector_code, m.factset_sector_desc AS sector FROM "
            + "factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + "JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c "
            + "ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = "
            + "d.factset_entity_id JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code "
            + "= d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON "
            + "f.tics_industry_code = e.tics_industry_code WHERE g.event_date BETWEEN :startDate AND :endDate AND "
            + "a.inception_date BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' AND "
            + "d.sector_code = :sectorCode AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code)) x GROUP BY "
            + "industry ORDER BY valuation_fx DESC";

    String pevcFundingDetailsBySectorCountryQuery = "SELECT sector_code as code, sector as name, SUM(valuation) AS valuation, SUM(valuation_fx) AS valuation_fx"
            + " ,:currency as target_currency,'Million' as unit FROM ((SELECT d.sector_code, m.factset_sector_desc AS sector, "
            + "SUM(a.valuation) / 1 AS valuation, SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, "
            + "a.inception_date)) / 1 AS valuation_fx FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM "
            + "cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector "
            + "d ON a.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = "
            + "d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON "
            + "f.tics_industry_code = e.tics_industry_code WHERE a.inception_date BETWEEN :startDate AND :endDate AND "
            + "a.portco_fin_type = 'vc' AND c.country_iso_code_2 = :countryIsoCode AND f.factset_industry LIKE '%Industrial%' GROUP BY "
            + "factset_sector_desc) UNION (SELECT d.sector_code, m.factset_sector_desc AS sector, SUM(h.transaction_value) AS valuation, "
            + "SUM(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx FROM "
            + "factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id JOIN "
            + "(SELECT country_iso_code_2, country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON "
            + "c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = "
            + "d.factset_entity_id JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = "
            + "a.factset_portco_entity_id JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN factset.ref_v2_factset_sector_map "
            + "m ON m.factset_sector_code = d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN "
            + "cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code WHERE g.event_date BETWEEN :startDate AND :endDate "
            + "AND a.inception_date BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' AND "
            + "c.country_iso_code_3 = :countryIsoCode AND f.factset_industry LIKE '%Industrial%' GROUP BY factset_sector_desc)) x GROUP BY "
            + "sector ORDER BY valuation_fx DESC";

    String pevcFundingDetailsBySectorIndustryCountryQuery = "SELECT tics_industry_code as code, industry as name, SUM(valuation) AS valuation, "
            + "SUM(valuation_fx) AS valuation_fx ,:currency as target_currency,'Million' as unit,sector_code, sector FROM ((SELECT f.tics_industry_code, "
            + "f.tics_industry_name AS industry, SUM(a.valuation) / 1 AS valuation, SUM(a.valuation * factset.get_fx_daily_conversion"
            + "(a.iso_currency, :currency, a.inception_date)) / 1 AS valuation_fx, d.sector_code, m.factset_sector_desc AS sector FROM factset.pe_v1_pe_securities a INNER JOIN "
            + "factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, "
            + "country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id JOIN "
            + "factset.ref_v2_factset_sector_map m ON m.factset_sector_code = d.sector_code JOIN cm.tics_industry_mapping e ON "
            + "d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code WHERE a.inception_date "
            + "BETWEEN :startDate AND :endDate AND a.portco_fin_type = 'vc' AND d.sector_code = :sectorCode AND c.country_iso_code_3 = :countryIsoCode "
            + "AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code) UNION (SELECT f.tics_industry_code, "
            + "f.tics_industry_name AS industry, SUM(h.transaction_value) AS valuation, SUM(h.transaction_value * "
            + "factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx ,d.sector_code, m.factset_sector_desc AS sector FROM factset.pe_v1_pe_securities a JOIN "
            + "factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, "
            + "country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id JOIN "
            + "factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id JOIN "
            + "factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = "
            + "d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON "
            + "f.tics_industry_code = e.tics_industry_code WHERE g.event_date BETWEEN :startDate AND :endDate AND a.inception_date "
            + "BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' AND d.sector_code = :sectorCode AND "
            + "f.factset_industry LIKE '%Industrial%' AND c.country_iso_code_3 = :countryIsoCode GROUP BY f.tics_industry_code)) x GROUP BY "
            + "industry ORDER BY valuation_fx DESC";

    String pevcFundingDetailsByAllSectorIndustryCountryQuery = "SELECT tics_industry_code as code, industry as name, SUM(valuation) AS valuation, "
            + "SUM(valuation_fx) AS valuation_fx ,:currency as target_currency,'Million' as unit ,sector_code, sector FROM "
            + "((SELECT f.tics_industry_code, f.tics_industry_name AS industry, SUM(a.valuation) / 1 AS valuation, "
            + "SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) / 1 AS valuation_fx, "
            + "d.sector_code, m.factset_sector_desc AS sector FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name "
            + "FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN "
            + "factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map "
            + "m ON m.factset_sector_code = d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN "
            + "cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code WHERE a.inception_date BETWEEN :startDate AND :endDate "
            + "AND a.portco_fin_type = 'vc' AND c.country_iso_code_3 = :countryIsoCode AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code) UNION (SELECT "
            + "f.tics_industry_code, f.tics_industry_name AS industry, SUM(h.transaction_value) AS valuation, SUM(h.transaction_value * "
            + "factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx,d.sector_code, m.factset_sector_desc "
            + "AS sector FROM factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = "
            + "b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name FROM cm.country_list GROUP BY "
            + "country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN factset.sym_v1_sym_entity_sector d ON "
            + "a.factset_portco_entity_id = d.factset_entity_id JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id "
            + "= a.factset_portco_entity_id JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN "
            + "factset.ref_v2_factset_sector_map m ON m.factset_sector_code = d.sector_code JOIN cm.tics_industry_mapping e ON "
            + "d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code WHERE g.event_date "
            + "BETWEEN :startDate AND :endDate AND a.inception_date BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' "
            + "AND h.ver = '1' AND c.country_iso_code_3 = :countryIsoCode AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code)) x GROUP BY industry ORDER BY "
            + "valuation_fx DESC";

    String pevcFundingDetailsByAllSectorIndustryGlobalQuery = "SELECT tics_industry_code as code, industry as name, SUM(valuation) AS valuation, "
            + "SUM(valuation_fx) AS valuation_fx, :currency as target_currency,'Million' as unit, sector_code, sector FROM "
            + "((SELECT f.tics_industry_code, f.tics_industry_name AS industry, SUM(a.valuation) / 1 AS valuation, "
            + "SUM(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) / 1 AS valuation_fx, "
            + "d.sector_code, m.factset_sector_desc AS sector FROM factset.pe_v1_pe_securities a INNER JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name "
            + "FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN "
            + "factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id JOIN factset.ref_v2_factset_sector_map "
            + "m ON m.factset_sector_code = d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN "
            + "cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code WHERE a.inception_date BETWEEN :startDate AND :endDate "
            + "AND a.portco_fin_type = 'vc' AND f.factset_industry LIKE '%Industrial%' GROUP BY f.tics_industry_code) UNION "
            + "(SELECT f.tics_industry_code, f.tics_industry_name AS industry, SUM(h.transaction_value) AS valuation, "
            + "SUM(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_fx, "
            + "d.sector_code, m.factset_sector_desc AS sector FROM factset.pe_v1_pe_securities a JOIN factset.sym_v1_sym_entity b "
            + "ON a.factset_portco_entity_id = b.factset_entity_id JOIN (SELECT country_iso_code_2, country_iso_code_3, country_name "
            + "FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country JOIN "
            + "factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id JOIN "
            + "factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id JOIN "
            + "factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id JOIN factset.ref_v2_factset_sector_map m ON m.factset_sector_code = "
            + "d.sector_code JOIN cm.tics_industry_mapping e ON d.industry_code = e.ff_ind_code JOIN cm.tics_industry f ON "
            + "f.tics_industry_code = e.tics_industry_code WHERE g.event_date BETWEEN :startDate AND :endDate AND a.inception_date "
            + "BETWEEN :startDate AND :endDate AND a.portco_fin_type != 'vc' AND h.ver = '1' AND f.factset_industry LIKE '%Industrial%' "
            + "GROUP BY f.tics_industry_code)) x GROUP BY industry ORDER BY valuation_fx DESC";


    String pevcFundDetailFinTypeNotSelectedCountry="select * from ((SELECT " +
            " c.*,sum(valuation) / 1 AS total_valuation " +
            "  FROM " +
            " factset.sym_v1_sym_entity a " +
            " JOIN " +
            " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
            " JOIN " +
            " (SELECT * " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " +
            "#industryJoin1 "+
            " where b.inception_date >= date_sub(curdate(),interval 5 year) " +
            " #industry #issueFilter1 and b.portco_fin_type='VC' " +
            " group by  a.iso_country) " +
            " union " +
            " (select " +
            " c.*,sum(transaction_value) " +
            " as total_valuation FROM " +
            " factset.pe_v1_pe_securities a " +
            " JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " +
            " JOIN (SELECT * " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country " +
            " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
            " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
            " #industryJoin2 "+
            " where a.inception_date >= date_sub(curdate(),interval 5 year) and g.event_date>= date_sub(curdate(),interval 5 year)" +
            " #industry  #issueFilter2 and a.portco_fin_type!='VC' "+
            " and transaction_value is not null and ver=1 group by  b.iso_country))x group by country_name  having sum(total_valuation) > 0 " +
            " order by country_name ";

    String pevcFundDetailFinTypeVCCountry="select * from ((SELECT " +
            " c.*,sum(valuation) / 1 AS total_valuation " +
            " FROM " +
            " factset.sym_v1_sym_entity a" +
            " JOIN " +
            " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
            " JOIN " +
            " (SELECT * " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " +
            " #industryJoin1 "+
            " where b.inception_date >= date_sub(curdate(),interval 5 year)  and valuation is not null "+
            " and b.portco_fin_type='VC' and valuation is not null " +
            "#industry  #issueFilter1 "+
            " group by  a.iso_country) )x group by country_name having sum(total_valuation) > 0 " +
            " order by country_name ";


    String pevcFundDetailFinTypePECountry="select * from (  "
            + "(select  "
            + "c.*,sum(transaction_value) as total_valuation  "
            + "FROM  factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + " JOIN (SELECT  country_iso_code_2, country_name ,country_iso_code_3,created_at,created_by,last_modified_at,last_modified_by,country_id,is_active,region_id " +
            " FROM  cm.country_list  GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "+
            " #industryJoin2 "+
            " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "+
            " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id  where a.inception_date >= date_sub(curdate(),interval 5 year) "+
            " and g.event_date>= date_sub(curdate(),interval 5 year)  and a.portco_fin_type='PE' and ver=1 and transaction_value is not null "+
            " #industry  #issueFilter2 "+
            " group by  b.iso_country))x group by country_name having sum(total_valuation) > 0   order by country_name";


    String pevcFundDetailFinTypeOTCountry="select * from (  "
            + "(select  "
            + "c.*,sum(transaction_value) as total_valuation  "
            + "FROM  factset.pe_v1_pe_securities a "
            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
            + " JOIN (SELECT  country_iso_code_2, country_name ,country_iso_code_3,created_at,created_by,last_modified_at,last_modified_by,country_id,is_active,region_id " +
            " FROM  cm.country_list  GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "+
            " #industryJoin2 "+
            " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "+
            " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id  where a.inception_date >= date_sub(curdate(),interval 5 year) "+
            " and g.event_date>= date_sub(curdate(),interval 5 year)  and a.portco_fin_type='OT' and ver=1 and transaction_value is not null "+
            " #industry  #issueFilter2 "+
            " group by  b.iso_country))x group by country_name having sum(total_valuation) > 0   order by country_name";


    String pevcFundDetailFinTypeVCPECountry="SELECT " +
            " country_id, " +
            " country_iso_code_2," +
            " country_iso_code_3, " +
            " country_name," +
            " region_id," +
            " is_active," +
            " created_at," +
            " created_by," +
            " last_modified_at," +
            " last_modified_by," +
            " total_valuation" +
            " FROM " +
            " (SELECT " +
            " *, GROUP_CONCAT(portco_fin_type) AS fin_type " +
            " FROM " +
            " ((SELECT " +
            " c.*," +
            " SUM(valuation ) / 1 AS total_valuation," +
            " b.portco_fin_type AS portco_fin_type " +
            " FROM " +
            " factset.sym_v1_sym_entity a " +
            " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
            " JOIN (SELECT " +
            " country_iso_code_2,country_iso_code_3,country_name,country_id ,region_id,is_active,created_at,created_by,last_modified_at,last_modified_by " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " +
            " #industryJoin1 "+
            " WHERE " +
            " b.inception_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
            " AND b.portco_fin_type = 'VC' and valuation is not null " +
            "#industry #issueFilter1 "+
            " GROUP BY a.iso_country) UNION (SELECT " +
            "  c.*," +
            " SUM(valuation ) / 1 AS total_valuation, " +
            " a.portco_fin_type AS fin_type " +
            " FROM " +
            " factset.pe_v1_pe_securities a " +
            " JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " +
            " JOIN (SELECT " +
            " country_iso_code_2,country_iso_code_3,country_name,country_id ,region_id,is_active,created_at,created_by,last_modified_at,last_modified_by " +
            " FROM " +
            " cm.country_list " +
            " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country " +
            " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
            " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
            " #industryJoin2 "+
            " WHERE " +
            " a.inception_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
            " AND g.event_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
            " AND a.portco_fin_type = 'PE' and transaction_value is not null and ver=1 " +
            "#industry  #issueFilter2 "+
            " GROUP BY b.iso_country)) x " +
            " GROUP BY country_name " +
            " HAVING SUM(total_valuation) > 0 " +
            "AND fin_type IN ('pe,vc' , 'vc,pe') " +
            "ORDER BY country_name) y ";


          String pevcAdvancedSearchFinancialType="select (@id\\:=@id + 1) AS id,x.* from(SELECT " +
                  " CASE " +
                  " WHEN fin_type = 'OT,PE,VC' THEN 'ALL' " +
                  " when fin_type='OT' then 'Other' " +
                  " ELSE fin_type " +
                  " END AS fin_type " +
                  " FROM " +
                  " (SELECT DISTINCT " +
                  " fin_type , case when fin_type='OT' then 100 else 1 end as display_order " +
                  " FROM " +
                  " (SELECT " +
                  " GROUP_CONCAT(DISTINCT portco_fin_type " +
                  " ORDER BY portco_fin_type) AS fin_type,b.factset_portco_entity_id " +
                  " FROM " +
                  " factset.pe_v1_pe_securities b  #issueFilter" +
                  " GROUP BY b.factset_portco_entity_id) x join factset.sym_v1_sym_entity a ON a.factset_entity_id = x.factset_portco_entity_id " +
                  " #industryJoin "+
                  " #countryJoin "+
                  " #industry #country)y " +
                  " WHERE " +
                  " fin_type NOT IN ('OT,VC' , 'OT,PE') " +
                  " ORDER BY  display_order ,fin_type limit 10)x JOIN(SELECT @id\\:=0) AS ai ";

        String pevcFundDetailFinTypeNotSelectedIssue = "select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                " g.issue_type_desc,g.issue_type_code " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN " +
                " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                "#industryJoin2 "+
                " JOIN  factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE issue_type is not null " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type " +
                " ORDER BY issue_type) x JOIN(SELECT @id\\:=0) AS ai ";


        String pevcFundDetailFinTypeVCIssue="select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                "g.issue_type_desc ,g.issue_type_code " +
                " FROM  "+
                " factset.pe_v1_pe_securities b  JOIN factset.sym_v1_sym_entity a on b.factset_portco_entity_id=a.factset_entity_id " +
                "#industryJoin2 "+
                " JOIN " +
                " factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE " +
                " b.portco_fin_type = 'VC' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type " +
                " ORDER BY issue_type limit 10) x JOIN(SELECT @id\\:=0) AS ai ";

        String pevcFundDetailFinTypePEIssue="select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                "g.issue_type_desc ,g.issue_type_code " +
                " FROM  "+
                " factset.pe_v1_pe_securities b  JOIN factset.sym_v1_sym_entity a on b.factset_portco_entity_id=a.factset_entity_id " +
                "#industryJoin2 "+
                " JOIN " +
                " factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE " +
                " b.portco_fin_type = 'PE' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type " +
                " ORDER BY issue_type limit 10) x JOIN(SELECT @id\\:=0) AS ai ";

        String pevcFundDetailFinTypeOTIssue="select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                "g.issue_type_desc ,g.issue_type_code " +
                " FROM  "+
                " factset.pe_v1_pe_securities b  JOIN factset.sym_v1_sym_entity a on b.factset_portco_entity_id=a.factset_entity_id " +
                "#industryJoin2 "+
                " JOIN " +
                " factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE " +
                " b.portco_fin_type = 'PE' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type " +
                " ORDER BY issue_type limit 10) x JOIN(SELECT @id\\:=0) AS ai ";

        String pevcFundDetailFinTypeVCPEIssue="select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                " d.issue_type_desc , d.issue_type_code " +
                " FROM " +
                " (SELECT " +
                " issue_type, GROUP_CONCAT(portco_fin_type) AS fin_type " +
                " FROM " +
                " ((SELECT " +
                " b.issue_type, portco_fin_type " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN " +
                " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " JOIN factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE " +
                " b.portco_fin_type = 'VC' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type) UNION (SELECT " +
                " b.issue_type, portco_fin_type " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN " +
                " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " JOIN factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin2 "+
                " WHERE " +
                "  b.portco_fin_type = 'pe' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type)) x " +
                " GROUP BY issue_type) y " +
                " JOIN " +
                " factset.ref_v2_issue_type_map d ON d.issue_type_code = y.issue_type " +
                " WHERE " +
                " fin_type IN ('PE,VC' , 'VC,PE') " +
                "LIMIT 10 ) x JOIN(SELECT @id\\:=0) AS ai ";


        String pevcFundDetailFinTypeNotSelectedIndustry="select tics_industry_name as ticsIndustryName,f.tics_industry_code as ticsIndustryCode from ((SELECT industry_code FROM " + 
        		" factset.sym_v1_sym_entity a " + 
        		" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
        		" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " + 
        		"#countryJoin2 "+
        	    " WHERE " + 
        		" portco_fin_type = 'VC' #issueFilter #countryFilter ) UNION (SELECT " + 
        		" industry_code " + 
        		" FROM " + 
        		" factset.sym_v1_sym_entity a " + 
        		" JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " + 
        		" JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id " + 
        		" JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " + 
        		"#countryJoin2 "+
                " WHERE " + 
        		" portco_fin_type !='VC' #issueFilter #countryFilter  and deal_id is not null )) d " + 
        		" JOIN (SELECT " + 
        		" tics_industry_code, ff_ind_code " + 
        		" FROM " + 
        		" cm.tics_industry_mapping " + 
        		" GROUP BY ff_ind_code) e ON d.industry_code = e.ff_ind_code " + 
        		" JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " + 
        		" GROUP BY tics_industry_name ";


        String pevcFundDetailFinTypeVCIndustry=" SELECT "+
                   " f.tics_industry_name as ticsIndustryName, f.tics_industry_code as ticsIndustryCode "+
                   " FROM "+
                   " (SELECT "+
                   " industry_code "+
                   " FROM "+
                   " (SELECT "+
                   " industry_code, a.iso_country "+
                   " FROM "+
                   " factset.sym_v1_sym_entity a "+
                   " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "+
                   " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id "+
                  " WHERE  "+
                  " portco_fin_type = 'VC' #issueFilter) x "+
                  "#countryJoin1 "+
                  " #countryFilter) y "+
                  " JOIN "+
                    "(SELECT "+
                           " tics_industry_code, ff_ind_code "+
                       " FROM "+
                          "  cm.tics_industry_mapping "+
                      "  GROUP BY ff_ind_code) e ON y.industry_code = e.ff_ind_code "+
                           " JOIN "+
                       " cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "+
                   " GROUP BY f.tics_industry_name "+
                  "  ORDER BY f.tics_industry_name ";

        String pevcFundDetailFinTypePEIndustry=" SELECT "+
               " f.tics_industry_name as ticsIndustryName, f.tics_industry_code as ticsIndustryCode "+
               " FROM "+
               " (SELECT "+
               " industry_code "+
               " FROM "+
               " (SELECT "+
               " industry_code, a.iso_country "+
               " FROM "+
               " factset.sym_v1_sym_entity a "+
               " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "+
               "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id " + 
              " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id "+
              " WHERE "+
              " portco_fin_type = 'PE' #issueFilter and deal_id is not null) x "+
              "#countryJoin1 "+
              " #countryFilter) y "+ 
              " JOIN "+
                "(SELECT "+
                       " tics_industry_code, ff_ind_code "+
                   " FROM "+
                      "  cm.tics_industry_mapping "+
                  "  GROUP BY ff_ind_code) e ON y.industry_code = e.ff_ind_code "+
                       " JOIN "+
                   " cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "+
               " GROUP BY f.tics_industry_name "+
              "  ORDER BY f.tics_industry_name ";


        String pevcFundDetailFinTypeOTIndustry=" SELECT "+
                   " f.tics_industry_name as ticsIndustryName, f.tics_industry_code as ticsIndustryCode "+
                   " FROM "+
                   " (SELECT "+
                   " industry_code "+
                   " FROM "+
                   " (SELECT "+
                   " industry_code, a.iso_country "+
                   " FROM "+
                   " factset.sym_v1_sym_entity a "+
                   " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id "+
                   " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id "+
                   " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id "+
                  " WHERE  "+
                  " portco_fin_type = 'OT' #issueFilter and deal_id is not null ) x "+
                  "#countryJoin1 "+
                  " #countryFilter) y "+
                  " JOIN "+
                    "(SELECT "+
                           " tics_industry_code, ff_ind_code "+
                       " FROM "+
                          "  cm.tics_industry_mapping "+
                      "  GROUP BY ff_ind_code) e ON y.industry_code = e.ff_ind_code "+
                           " JOIN "+
                       " cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "+
                   " GROUP BY f.tics_industry_name "+
                  "  ORDER BY f.tics_industry_name ";

        String pevcFundDetailFinTypeVCPEIndustry=" SELECT " +
                " tics_industry_name as ticsIndustryName, tics_industry_code as ticsIndustryCode " +
                " FROM " +
                " (SELECT " +
                " f.tics_industry_name," +
                " f.tics_industry_code," +
                " GROUP_CONCAT(DISTINCT portco_fin_type) AS fin_type " +
                " FROM " +
                " (SELECT " +
                "  *" +
                " FROM " +
                " ((SELECT " +
                " factset_portco_entity_id," +
                " industry_code," +
                " portco_fin_type," +
                " a.iso_country " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " +
                " WHERE " +
                " portco_fin_type = 'VC' #issueFilter) UNION (SELECT " +
                " b.factset_portco_entity_id," +
                " industry_code, " +
                " portco_fin_type, " +
                " a.iso_country " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id "+
                " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " +
                " WHERE " +
                " portco_fin_type = 'PE' #issueFilter and deal_id is not null)) d " +
                "#countryJoin1 " +
                " #countryFilter ) y " +
                " JOIN (SELECT " +
                " tics_industry_code, ff_ind_code " +
                " FROM " +
                " cm.tics_industry_mapping " +
                " GROUP BY ff_ind_code) e ON y.industry_code = e.ff_ind_code " +
                " JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " +
                " GROUP BY tics_industry_name) x " +
                " WHERE " +
                " fin_type IN ('PE,VC' , 'VC,PE')";


        String pevcFundDetailFinTypeNotSelectedFxAmount=
                " SELECT " +
                " MAX(total_valuation) AS maxAmount," +
                " MIN(total_valuation) AS minAmount " +
                " FROM " +
                " (SELECT " +
                " SUM(total_valuation) AS total_valuation" +
                " FROM " +
                " ((SELECT " +
                " SUM((valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date))) / 1 AS total_valuation," +
                " factset_portco_entity_id " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " #countryJoin1 "+
                " WHERE " +
                " portco_fin_type = 'VC' #companyFilter1 " +
                " AND valuation IS NOT NULL " +
                " AND valuation > 0 " +
                " #industryFilter1 "+
                " #issueFilter1 "+
                " #countryFilter1 "+
                " GROUP BY factset_portco_entity_id) UNION (SELECT " +
                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation," +
                " b.factset_portco_entity_id " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin2 #countryJoin2 "+
                " WHERE  portco_fin_type != 'VC' " +
                " #companyFilter2 " +
                " AND transaction_value IS NOT NULL " +
                " AND ver = 1 " +
                " AND h.deal_id IS NOT NULL " +
                " AND transaction_value > 0 " +
                "#industryFilter2 "+
                " #issueFilter2 "+
                " #countryFilter2 "+
                " GROUP BY b.factset_portco_entity_id)) x " +
                " GROUP BY factset_portco_entity_id) y " +
                " WHERE " +
                " total_valuation IS NOT NULL";

        String pevcFundDetailFinTypePEAmount=" SELECT " +
                " MAX(total_valuation) AS maxAmount," +
                " MIN(total_valuation) AS minAmount " +
                " FROM " +
                " (SELECT " +
                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation, " +
                " a.factset_portco_entity_id " +
                " FROM " +
                 "factset.pe_v1_pe_securities a  JOIN factset.sym_v1_sym_entity b on a.factset_portco_entity_id=b.factset_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin1 "+
                " #countryJoin1 "+
                " WHERE portco_fin_type = 'PE' " +
                " #companyFilter1  #issueFilter1 #industryFilter1" +
                " #countryFilter1 "+
                " AND transaction_value IS NOT NULL " +
                " AND ver = 1 " +
                " AND h.deal_id IS NOT NULL " +
                " AND transaction_value > 0 " +
                " GROUP BY a.factset_portco_entity_id) x " +
                " WHERE " +
                " total_valuation IS NOT NULL " ;

        String pevcFundDetailFinTypeVCFxAmount=" SELECT " +
                " MAX(total_valuation) AS maxAmount," +
                " MIN(total_valuation) AS minAmount " +
                " FROM " +
                " (SELECT " +
                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation," +
                " a.factset_portco_entity_id " +
                " FROM " +
                 "factset.pe_v1_pe_securities a  JOIN factset.sym_v1_sym_entity b on a.factset_portco_entity_id=b.factset_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin1 "+
                " #countryJoin1 "+
                " WHERE portco_fin_type = 'VC' " +
                " #companyFilter1  #issueFilter1 #industryFilter1" +
                " #countryFilter1 "+
                " AND transaction_value IS NOT NULL " +
                " AND ver = 1 " +
                " AND h.deal_id IS NOT NULL " +
                " AND transaction_value > 0 " +
                " GROUP BY a.factset_portco_entity_id) x " +
                " WHERE " +
                " total_valuation IS NOT NULL " ;



        String pevcFundDetailFinTypeOTAmount=" SELECT " +
                " MAX(total_valuation) AS maxAmount," +
                " MIN(total_valuation) AS minAmount " +
                " FROM " +
                " (SELECT " +
                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation," +
                " a.factset_portco_entity_id " +
                " FROM " +
                 " factset.pe_v1_pe_securities a  JOIN factset.sym_v1_sym_entity b on a.factset_portco_entity_id=b.factset_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin1 "+
                " #countryJoin1 "+
                " WHERE portco_fin_type = 'OT' " +
                " #companyFilter1  #issueFilter1 #industryFilter1" +
                " #countryFilter1 "+
                " AND transaction_value IS NOT NULL " +
                " AND ver = 1 " +
                " AND h.deal_id IS NOT NULL " +
                " AND transaction_value > 0 " +
                " GROUP BY a.factset_portco_entity_id) x " +
                " WHERE " +
                " total_valuation IS NOT NULL " ;

        String pevcFundDetailFinTypeVCPEAmount=" SELECT " +
                " MAX(total_valuation) AS  maxAmount ," +
                " MIN(total_valuation) AS  minAmount " +
                " FROM " +
                " (SELECT " +
                " SUM(total_valuation) AS total_valuation, " +
                " GROUP_CONCAT(portco_fin_type) AS fin_type " +
                " FROM " +
                " ((SELECT " +
                " SUM((valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date))) / 1 AS total_valuation, " +
                " factset_portco_entity_id, " +
                " portco_fin_type " +
                " FROM  factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " #countryJoin1 "+
                " WHERE " +
                " portco_fin_type = 'VC' " +
                " #companyFilter1  #issueFilter1 #industryFilter1" +
                " #countryFilter1 "+
                " AND valuation IS NOT NULL " +
                " AND valuation > 0 " +
                " GROUP BY factset_portco_entity_id) UNION (SELECT " +
                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation," +
                " b.factset_portco_entity_id," +
                " portco_fin_type " +
                " FROM  factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin2 #countryJoin2 "+
                " WHERE portco_fin_type = 'PE'" +
                " #companyFilter2 " +
                " AND transaction_value IS NOT NULL " +
                " AND ver = 1 " +
                " AND h.deal_id IS NOT NULL " +
                " AND transaction_value > 0 " +
                " #industryFilter2 "+
                " #issueFilter2 "+
                " #countryFilter2 "+
                " GROUP BY b.factset_portco_entity_id)) x " +
                " GROUP BY factset_portco_entity_id) y " +
                " WHERE " +
                " fin_type IN ('PE,VC' , 'VC,PE') " +
                " AND total_valuation IS NOT NULL";
        
        
        
        String pevcFundDetailFinTypeMultipleCountry="SELECT " +
                " country_id, " +
                " country_iso_code_2," +
                " country_iso_code_3, " +
                " country_name," +
                " region_id," +
                " is_active," +
                " created_at," +
                " created_by," +
                " last_modified_at," +
                " last_modified_by," +
                " total_valuation" +
                " FROM " +
                " (SELECT " +
                " *, GROUP_CONCAT(portco_fin_type) AS fin_type " +
                " FROM " +
                " ((SELECT " +
                " c.*," +
                " SUM(valuation ) / 1 AS total_valuation," +
                " b.portco_fin_type AS portco_fin_type " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN (SELECT " +
                " country_iso_code_2,country_iso_code_3,country_name,country_id ,region_id,is_active,created_at,created_by,last_modified_at,last_modified_by " +
                " FROM " +
                " cm.country_list " +
                " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = a.iso_country " +
                " #industryJoin1 "+
                " WHERE " +
                " b.inception_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
                " AND b.portco_fin_type = 'VC' and valuation is not null " +
                "#industry #issueFilter1 "+
                " GROUP BY a.iso_country) UNION (SELECT " +
                "  c.*," +
                " SUM(valuation ) / 1 AS total_valuation, " +
                " a.portco_fin_type AS fin_type " +
                " FROM " +
                " factset.pe_v1_pe_securities a " +
                " JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id " +
                " JOIN (SELECT " +
                " country_iso_code_2,country_iso_code_3,country_name,country_id ,region_id,is_active,created_at,created_by,last_modified_at,last_modified_by " +
                " FROM " +
                " cm.country_list " +
                " GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id " +
                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
                " #industryJoin2 "+
                " WHERE " +
                " a.inception_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
                " AND g.event_date >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) " +
                " AND a.portco_fin_type !='VC' and transaction_value is not null and ver=1 " +
                "#industry  #issueFilter2 "+
                " GROUP BY b.iso_country)) x " +
                " GROUP BY country_name " +
                " HAVING SUM(total_valuation) > 0 " +
                "AND fin_type IN (:multipleFinType) " +
                "ORDER BY country_name) y ";
        
        
        
        String pevcFundDetailFinTypeMultiPleIndustry=" SELECT " +
                " tics_industry_name as ticsIndustryName, tics_industry_code as ticsIndustryCode " +
                " FROM " +
                " (SELECT " +
                " f.tics_industry_name," +
                " f.tics_industry_code," +
                " GROUP_CONCAT(DISTINCT portco_fin_type) AS fin_type " +
                " FROM " +
                " (SELECT " +
                "  *" +
                " FROM " +
                " ((SELECT " +
                " factset_portco_entity_id," +
                " industry_code," +
                " portco_fin_type," +
                " a.iso_country " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " +
                " WHERE " +
                " portco_fin_type = 'VC' #issueFilter) UNION (SELECT " +
                " b.factset_portco_entity_id," +
                " industry_code, " +
                " portco_fin_type, " +
                " a.iso_country " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id "+
                " JOIN factset.sym_v1_sym_entity_sector d ON d.factset_entity_id = b.factset_portco_entity_id " +
                " WHERE " +
                " portco_fin_type != 'VC' #issueFilter)) d " +
                "#countryJoin1 " +
                " #countryFilter ) y " +
                " JOIN (SELECT " +
                " tics_industry_code, ff_ind_code " +
                " FROM " +
                " cm.tics_industry_mapping " +
                " GROUP BY ff_ind_code) e ON y.industry_code = e.ff_ind_code " +
                " JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code " +
                " GROUP BY tics_industry_name) x " +
                " WHERE " +
                " fin_type IN (:multipleFinType)";

		String pevcFundDetailFinTypeMultiIssue = "select(@id\\:=@id + 1) AS id,x.* from(SELECT " +
                " d.issue_type_desc , d.issue_type_code " +
                " FROM " +
                " (SELECT " +
                " issue_type, GROUP_CONCAT(portco_fin_type) AS fin_type " +
                " FROM " +
                " ((SELECT " +
                " b.issue_type, portco_fin_type " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN " +
                " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " JOIN factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin1 "+
                " WHERE " +
                " b.portco_fin_type = 'VC' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type) UNION (SELECT " +
                " b.issue_type, portco_fin_type " +
                " FROM " +
                " factset.sym_v1_sym_entity a " +
                " JOIN " +
                " factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
                " #industryJoin2 "+
                " JOIN factset.ref_v2_issue_type_map g ON g.issue_type_code = b.issue_type " +
                " #countryJoin2 "+
                " WHERE " +
                "  b.portco_fin_type != 'vc' " +
                " #industryFilter #countryilter  " +
                " GROUP BY issue_type)) x " +
                " GROUP BY issue_type) y " +
                " JOIN " +
                " factset.ref_v2_issue_type_map d ON d.issue_type_code = y.issue_type " +
                " WHERE " +
                " fin_type IN (:multipleFinType) " +
                "LIMIT 10 ) x JOIN(SELECT @id\\:=0) AS ai ";
		
		
		
		
		 String pevcFundDetailFinTypeMultipleAmount=" SELECT " +
	                " MAX(total_valuation) AS  maxAmount ," +
	                " MIN(total_valuation) AS  minAmount " +
	                " FROM " +
	                " (SELECT " +
	                " SUM(total_valuation) AS total_valuation, " +
	                " GROUP_CONCAT(portco_fin_type) AS fin_type " +
	                " FROM " +
	                " ((SELECT " +
	                " SUM((valuation * factset.get_fx_daily_conversion(iso_currency, :currency, inception_date))) / 1 AS total_valuation, " +
	                " factset_portco_entity_id, " +
	                " portco_fin_type " +
	                " FROM  factset.sym_v1_sym_entity a " +
	                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
	                " #industryJoin2 "+
	                " #countryJoin1 "+
	                " WHERE " +
	                " portco_fin_type = 'VC' " +
	                " #companyFilter1  #issueFilter1 #industryFilter1" +
	                " #countryFilter1 "+
	                " AND valuation IS NOT NULL " +
	                " AND valuation > 0 " +
	                " GROUP BY factset_portco_entity_id) UNION (SELECT " +
	                " SUM(transaction_value * factset.get_fx_daily_conversion(iso_currency, :currency, event_date)) AS total_valuation," +
	                " b.factset_portco_entity_id," +
	                " portco_fin_type " +
	                " FROM  factset.sym_v1_sym_entity a " +
	                " JOIN factset.pe_v1_pe_securities b ON a.factset_entity_id = b.factset_portco_entity_id " +
	                " JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = b.factset_portco_entity_id " +
	                " JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id " +
	                " #industryJoin2 #countryJoin2 "+
	                " WHERE portco_fin_type = 'PE'" +
	                " #companyFilter2 " +
	                " AND transaction_value IS NOT NULL " +
	                " AND ver = 1 " +
	                " AND h.deal_id IS NOT NULL " +
	                " AND transaction_value > 0 " +
	                " #industryFilter2 "+
	                " #issueFilter2 "+
	                " #countryFilter2 "+
	                " GROUP BY b.factset_portco_entity_id)) x " +
	                " GROUP BY factset_portco_entity_id) y " +
	                " WHERE " +
	                " fin_type IN (:multi) " +
	                " AND total_valuation IS NOT NULL";
	               

		String pevcFundDetailFinTypeMultiple = "select (@id\\:=@id + 1) AS id, M.* from (select * from (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,"
	            + "tics_industry_name,category_name_desc,inception_date,GROUP_CONCAT(portco_fin_type) AS portco_fin_type,sum(valuation) as valuation,sum(valuation_cal) as valuation_cal,target_currency,'Million' AS unit,SUM(count) AS count,company_id,entity_type "
	            + "FROM ((SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
	            + "SUM(valuation)/ 1  AS valuation,SUM((valuation_cal)) / 1 AS valuation_cal,target_currency,COUNT(inception_date) as count,company_id,entity_type "
	            + "FROM( select b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,"
	            + "f.tics_industry_name,a.category_name_desc,a.inception_date,a.portco_fin_type,a.valuation,"
	            + "(a.valuation) * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date) as valuation_cal,:currency AS target_currency,company_id,entity_type_desc as entity_type "
	            + "FROM factset.pe_v1_pe_securities a "
	            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
	            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
	            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
	            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
	            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
	            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
	            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
	            + "WHERE a.inception_date between :startDate AND :endDate #country_industry_entity_filter_template "
	            + "AND a.portco_fin_type = 'vc' and a.iso_currency is not null  #issue-filter  order by a.inception_date desc)x  GROUP BY entity_proper_name) "
	            + "UNION (SELECT factset_entity_id,entity_proper_name,country_iso_code_3,country_name,tics_industry_code,tics_industry_name,category_name_desc,MAX(inception_date) AS inception_date,portco_fin_type,"
	            + "SUM(valuation) AS valuation,SUM(valuation_cal) AS valuation_cal,target_currency,COUNT(inception_date) as count,company_id, entity_type "
	            + "FROM (SELECT b.factset_entity_id,b.entity_proper_name,c.country_iso_code_3,c.country_name,f.tics_industry_code,"
	            + "f.tics_industry_name,a.category_name_desc,g.event_date AS inception_date,a.portco_fin_type,h.transaction_value AS valuation,"
	            + "h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date) AS valuation_cal,:currency AS target_currency,company_id,entity_type_desc as entity_type "
	            + "FROM factset.pe_v1_pe_securities a "
	            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
	            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
	            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
	            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
	            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
	            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
	            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
	            + "join factset.ref_v2_entity_type_map m on entity_type_code=b.entity_type "
	            + "left join (select * from (select * from company_list #entityIdFilter order by domicile_flag desc )x group by factset_entity_id) cl on cl.factset_entity_id=a.factset_portco_entity_id "
	            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
	            + "#country_industry_entity_filter_template AND a.portco_fin_type != 'VC' "
	            + "AND h.ver = '1'  AND a.iso_currency IS NOT NULL #issue-filter AND g.deal_id is not null order by a.inception_date desc)x  "
	            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name)v "
	            + "WHERE (portco_fin_type in (:multipleFinType)) AND valuation_cal BETWEEN :minAmount AND :maxAmount or valuation_cal is null ORDER BY #sorting_template) M  JOIN (SELECT @id\\:=:rowOffset) AS ai limit :rowOffset,:rowCount";

		String pevcFundDetailFinTypeMultiCount = "select count(*) from (select * from (SELECT entity_proper_name,GROUP_CONCAT(portco_fin_type) AS fin_type,sum(valuation_cal) as valuation_cal "
	            + "FROM ((SELECT entity_proper_name,portco_fin_type,(sum(valuation_cal) / 1) AS valuation_cal "
	            + "FROM( select b.entity_proper_name,(a.valuation * factset.get_fx_daily_conversion(a.iso_currency, :currency, a.inception_date)) AS valuation_cal, a.portco_fin_type " + "FROM factset.pe_v1_pe_securities a "
	            + "INNER JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
	            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
	            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
	            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
	            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
	            + "WHERE a.inception_date between :startDate AND :endDate #country_industry_entity_filter_template "
	            + "AND a.portco_fin_type = 'vc' and a.iso_currency is not null  #issue-filter order by a.inception_date desc)x GROUP BY entity_proper_name) "
	            + "UNION (SELECT entity_proper_name,portco_fin_type,sum(valuation_cal) as valuation_cal "
	            + "FROM (SELECT b.entity_proper_name,(h.transaction_value * factset.get_fx_daily_conversion(a.iso_currency, :currency, g.event_date)) AS valuation_cal,a.portco_fin_type " + "FROM factset.pe_v1_pe_securities a "
	            + "JOIN factset.sym_v1_sym_entity b ON a.factset_portco_entity_id = b.factset_entity_id "
	            + "JOIN (SELECT country_iso_code_2,country_iso_code_3, country_name FROM cm.country_list GROUP BY country_iso_code_2) c ON c.country_iso_code_2 = b.iso_country "
	            + "JOIN factset.sym_v1_sym_entity_sector d ON a.factset_portco_entity_id = d.factset_entity_id "
	            + "JOIN (select tics_industry_code,ff_ind_code from cm.tics_industry_mapping group by ff_ind_code) e ON d.industry_code = e.ff_ind_code "
	            + "JOIN cm.tics_industry f ON f.tics_industry_code = e.tics_industry_code "
	            + "JOIN factset.pe_v1_pe_portco_pvt_invest_evts g ON g.factset_portco_entity_id = a.factset_portco_entity_id "
	            + "JOIN factset.ma_v1_ma_deal_terms h ON h.deal_id = g.deal_id "
	            + "WHERE g.event_date between :startDate AND :endDate AND (a.inception_date between :startDate AND :endDate or a.inception_date is null) "
	            + "#country_industry_entity_filter_template AND a.portco_fin_type != 'VC' "
	            + "AND h.ver = '1'  AND a.iso_currency IS NOT NULL #issue-filter AND g.deal_id is not null order by a.inception_date desc)x "
	            + "GROUP BY entity_proper_name)) z GROUP BY entity_proper_name)v "
	            + "WHERE (fin_type in (:multipleFinType) AND  valuation_cal between :minAmount and :maxAmount or valuation_cal is null )) M   ";





}

