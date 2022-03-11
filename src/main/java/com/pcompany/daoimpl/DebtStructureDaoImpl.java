package com.pcompany.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dao.DebtStructureDao;
import com.pcompany.dto.DebtCapitalStructureDetailsDTO;
import com.pcompany.entity.DebtCapitalStructureDetails;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class DebtStructureDaoImpl implements DebtStructureDao{
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebt(String entityId) {
		
		/*String baseQuery=" select (@id \\:=@id+1)as id ,factset_entity_id,instrument_description,Date_format(issue_date,'%d-%b-%Y') AS issue_date,coupon_type, coupon_index,asset_desc,collateral_type,issue_amount,os_amount ,avail_amount,instrument_type,  maturity_date,null as maturity_year, min_coupon_rate,max_coupon_rate,coupon_spread, currency, report_date as report_date ,active as active,instrument_id,issue_type from (select * from (select factset_entity_id,instrument_description,issue_date,coupon_type, coupon_index,asset_desc,collateral_type,issue_amount, os_amount,avail_amount,instrument_type,maturity_date, min_coupon_rate,max_coupon_rate, coupon_spread,currency,report_date ,active,instrument_id,issue_type,   case  when timestampdiff(day,curdate(),maturity_date) < 0 then'z'  when timestampdiff(day,curdate(),maturity_date) is null then 'y'  else 'a' end as display_order from (  select factset_entity_id,instrument_description,issue_date,coupon_desc as coupon_type, coupon_index,asset_desc,collateral_type,issue_amount, os_amount,( issue_amount - os_amount ) AS avail_amount, dm.debt_desc as instrument_type ,  case "+
				" when maturity_year is null then null "+
				" when maturity_year_month is null then maturity_year "+
				" when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0)) "+
				" else "+
				" concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS maturity_date,  coupon_rate_min  AS min_coupon_rate, coupon_rate_max  AS max_coupon_rate,  coupon_margin_min  AS coupon_spread,  issuance_currency as currency, report_date as report_date ,active as active,instrument_id,dm.issue_type  from `dcs_v2_dcs_details` d  left JOIN     ref_v2_dcs_coupon_map m ON       m.coupon_code = d.coupon_code    JOIN ref_v2_dcs_debt_map dm    ON       dm.debt_code = d.debt_code WHERE factset_entity_id = :entity and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id  order by active desc,display_order asc,maturity_date asc)y where os_amount is not null )z join (select @id \\:=0) a  ";*/
		
		String baseQuery=" select (@id \\:=@id+1)as id ,factset_entity_id,instrument_description,Date_format(issue_date,'%d-%b-%Y') AS issue_date,coupon_type, coupon_index,asset_desc,collateral_type,issue_amount,os_amount ,avail_amount,instrument_type,  maturity_date,null as maturity_year, min_coupon_rate,max_coupon_rate,coupon_spread, issuance_currency, currency, report_date as report_date ,active as active,instrument_id,issue_type from (select * from (select factset_entity_id,instrument_description,issue_date,coupon_type, coupon_index,asset_desc,collateral_type,issue_amount, os_amount,avail_amount,instrument_type,maturity_date, min_coupon_rate,max_coupon_rate, coupon_spread, issuance_currency, currency,report_date ,active,instrument_id,issue_type, "+
 
		" case  when timestampdiff(day,curdate(),default_maturity_date) < 0 then'z'  when timestampdiff(day,curdate(),default_maturity_date) is null then 'y'  else 'a' end as display_order,maturity_year,maturity_year_month,maturity_year_day "+
		" from (  select factset_entity_id,instrument_description,issue_date,coupon_desc as coupon_type, coupon_index,asset_desc,collateral_type,issue_amount, os_amount,( issue_amount - os_amount ) AS avail_amount, dm.debt_desc as instrument_type ,  case "+
		" when maturity_year is null then null "+
		" when maturity_year_month is null then maturity_year "+
		" when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0)) "+
		" else "+
		" concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS maturity_date,case "+
		" when maturity_year is null then null "+
		" when maturity_year_month is null then concat(maturity_year,'-12-31') "+
		" when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0),'-28') "+
		" else "+
		" concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS default_maturity_date,maturity_year,maturity_year_month,maturity_year_day, coupon_rate_min  AS min_coupon_rate, coupon_rate_max  AS max_coupon_rate,  coupon_margin_min  AS coupon_spread,  issuance_currency, currency, report_date as report_date ,active as active,instrument_id,dm.issue_type  from `dcs_v2_dcs_details` d  left JOIN     ref_v2_dcs_coupon_map m ON       m.coupon_code = d.coupon_code    JOIN ref_v2_dcs_debt_map dm    ON       dm.debt_code = d.debt_code WHERE factset_entity_id = :entity and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id  order by active desc,display_order asc, maturity_year asc,maturity_year_month asc ,maturity_year_day asc )y where os_amount is not null )z join (select @id \\:=0)a ";

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DebtCapitalStructureDetails.class);
		query.setParameter("entity", entityId);

		
		@SuppressWarnings("unchecked")
		List<DebtCapitalStructureDetails> data = (List<DebtCapitalStructureDetails>) query.list();
		System.out.println(data.size());

		List<DebtCapitalStructureDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, DebtCapitalStructureDetailsDTO.class);	

		return dcsDTOs;
		//return null;
	}

	@Override
	public List<DebtCapitalStructureDetailsDTO> getBorrowingLimit(String entityId) {
		
		/*String baseQuery="select (@id \\:=@id+1)as id ,null as maturity_year, z.* from (select sum(os_amount) as os_amount,instrument_type,null as id ,null as factset_entity_id,null as instrument_description,null as issue_date,null as coupon_type, "+
                         " null as coupon_index,null as asset_desc,null as collateral_type,null as issue_amount,null as  avail_amount, "+
                         " maturity_date,null as min_coupon_rate,null as max_coupon_rate, "+
                         "  null as coupon_spread,null as currency, null as report_date ,active as active,null as instrument_id,null as issue_type from(select * from (select d.os_amount, dm.debt_desc as instrument_type,d.instrument_id,concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) AS maturity_date,d.active as active from dcs_v2_dcs_details d "+
                         " left JOIN     ref_v2_dcs_coupon_map m "+
                         "   ON       m.coupon_code = d.coupon_code "+
                         "   JOIN     ref_v2_dcs_debt_map dm "+
                         "   ON       dm.debt_code = d.debt_code "+
                         "   WHERE factset_entity_id = :entityId and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id order by active desc,maturity_date)y where os_amount is not null group by instrument_type)z join (select @id \\:=0)a ";*/
		
		String baseQuery=" select (@id \\:=@id+1)as id ,null as maturity_year, z.* from (select sum(os_amount) as os_amount, "+
				 " instrument_type,null as id ,null as factset_entity_id,null as instrument_description,null as issue_date,null as coupon_type, "+
				 " null as coupon_index,null as asset_desc,null as collateral_type,null as issue_amount,null as avail_amount,  maturity_date,null as min_coupon_rate, "+
				 " null as max_coupon_rate,   null as coupon_spread, issuance_currency, currency, null as report_date ,active as active,null as instrument_id,null as issue_type from(select * from (select d.os_amount, dm.debt_desc as instrument_type,d.instrument_id, issuance_currency, currency, case "+
				 " when maturity_year is null then null "+
				 " when maturity_year_month is null then maturity_year "+
				 " when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0)) "+
				 " else "+
				 " concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS maturity_date,d.active as active from dcs_v2_dcs_details d left JOIN     ref_v2_dcs_coupon_map m    ON       m.coupon_code = d.coupon_code    JOIN     ref_v2_dcs_debt_map dm    ON dm.debt_code = d.debt_code    WHERE factset_entity_id = :entityId and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id order by active desc,maturity_date)y where os_amount is not null and os_amount > 0 group by instrument_type)z join (select @id \\:=0)a ";

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DebtCapitalStructureDetails.class);
		query.setParameter("entityId", entityId);

		@SuppressWarnings("unchecked")
		List<DebtCapitalStructureDetails> data = (List<DebtCapitalStructureDetails>) query.list();

		List<DebtCapitalStructureDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, DebtCapitalStructureDetailsDTO.class);	

		return dcsDTOs;
	}

	@Override
	public List<DebtCapitalStructureDetailsDTO> getCapitalCharges(String entityId) {
		String baseQuery= "SELECT (@row_number\\:=@row_number + 1) AS id,factset_entity_id as entity_id ,instrument_description ,issue_date,coupon_code as coupon_type,coupon_index,asset_desc,issue_amount,os_amount,debt_code as instrument_type, issue_date as maturity_date, coupon_rate_min as coupon_rate,coupon_margin_min as coupon_spread, issuance_currency, currency FROM `dcs_v2_dcs_details`, (SELECT @row_number\\:=0) AS t where factset_entity_id=:entityId";

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DebtCapitalStructureDetails.class);
		query.setParameter("entityId", entityId);

		@SuppressWarnings("unchecked")
		List<DebtCapitalStructureDetails> data = (List<DebtCapitalStructureDetails>) query.list();

		List<DebtCapitalStructureDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, DebtCapitalStructureDetailsDTO.class);	

		return dcsDTOs;
	}

	@Override
	public List<DebtCapitalStructureDetailsDTO> getEntityMaturity(String entityId) {
		
		/*String baseQuery=" SELECT (@id \\:=@id+1)AS id , factset_entity_id,instrument_description, issue_date, "+
       " coupon_type, coupon_index, asset_desc, collateral_type, issue_amount, os_amount , avail_amount, instrument_type, "+ 
       " maturity_year, maturity_date, min_coupon_rate, max_coupon_rate, coupon_spread, currency, active, instrument_id, issue_type "+ 
       " FROM   ( "+

	" select sum(os_amount) as os_amount,instrument_type,maturity_year,null as factset_entity_id,null as instrument_description,null as issue_date,null as coupon_type, "+
                         " null as coupon_index,null as asset_desc,null as collateral_type,null as issue_amount,null as  avail_amount, "+

                          " maturity_date, "+
                         " null as min_coupon_rate,null as max_coupon_rate,null as coupon_spread, "+
                         " issuance_currency as currency, null as report_date ,active as active,null as instrument_id,null as issue_type from(select * from (select d.os_amount, dm.debt_desc as instrument_type,d.instrument_id,concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) AS maturity_date,d.active as active,d.maturity_year,issuance_currency from `dcs_v2_dcs_details` d "+
                         " left JOIN     ref_v2_dcs_coupon_map m ON       m.coupon_code = d.coupon_code "+
                          "  JOIN     ref_v2_dcs_debt_map dm ON       dm.debt_code = d.debt_code "+
                       
					" WHERE factset_entity_id = :entity and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id order by active desc,maturity_date)y where os_amount is not null group by instrument_type,maturity_year )z "+ 
					" JOIN ( SELECT @id \\:=0)a ";*/
		
		String baseQuery=" SELECT (@id \\:=@id+1)AS id , factset_entity_id,instrument_description, issue_date, coupon_type, "+
				 " coupon_index, asset_desc, collateral_type, issue_amount, os_amount , avail_amount, instrument_type, maturity_year, maturity_date, min_coupon_rate, max_coupon_rate, coupon_spread, issuance_currency, currency, active, instrument_id, issue_type  FROM (  select sum(os_amount) as os_amount,instrument_type,maturity_year,null as factset_entity_id,null as instrument_description,null as issue_date,null as coupon_type,  null as coupon_index,null as asset_desc,null as collateral_type,null as issue_amount,null as avail_amount,  maturity_date,  null as min_coupon_rate,null as max_coupon_rate,null as coupon_spread, issuance_currency, currency, null as report_date ,active as active,null as instrument_id,null as issue_type from(select * from (select d.os_amount, dm.debt_desc as instrument_type,d.instrument_id,case "+
				 " when maturity_year is null then null "+
				 " when maturity_year_month is null then maturity_year "+
				 " when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0)) "+
				 " else "+
				 " concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS maturity_date,d.active as active,d.maturity_year, issuance_currency, currency from `dcs_v2_dcs_details` d  left JOIN     ref_v2_dcs_coupon_map m ON       m.coupon_code = d.coupon_code   JOIN     ref_v2_dcs_debt_map dm ON dm.debt_code = d.debt_code  WHERE factset_entity_id = :entity and dm.issue_type !='FX' ORDER BY `report_date` DESC)x  group by instrument_id order by active desc,maturity_date)y where os_amount is not null and os_amount > 0 group by instrument_type,maturity_year )z  JOIN ( SELECT @id \\:=0)a "; 

		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DebtCapitalStructureDetails.class);
		query.setParameter("entity", entityId);

		
		@SuppressWarnings("unchecked")
		List<DebtCapitalStructureDetails> data = (List<DebtCapitalStructureDetails>) query.list();
		System.out.println(data);

		List<DebtCapitalStructureDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, DebtCapitalStructureDetailsDTO.class);	

		return dcsDTOs;
	}

	@Override
	public List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebtDownload(String entityId) {
		
		/*String baseQuery="select (@id \\:=@id+1) as id ,factset_entity_id,instrument_description,issue_date,coupon_type, "+
                         " coupon_index,asset_desc,collateral_type,issue_amount,null as maturity_year, "+
                         "  os_amount,avail_amount,instrument_type,maturity_date,min_coupon_rate,max_coupon_rate, "+
                         "  coupon_spread, max_coupon_margin, "+
                         " currency, report_date as report_date ,active as active,instrument_id  from (select * from (select * from( "+
                         " select  factset_entity_id,instrument_description,issue_date,coupon_desc as coupon_type, "+
                         " coupon_index,asset_desc,collateral_type,issue_amount, "+
                         "  os_amount, "+
                         " ( issue_amount - os_amount ) AS avail_amount, "+
                         " dm.debt_desc as instrument_type , "+
                         "	concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) AS maturity_date, "+
                         " coupon_rate_min  AS min_coupon_rate, "+
                         " coupon_rate_max  AS max_coupon_rate, "+
                         " coupon_margin_min  AS coupon_spread, "+
                         " coupon_margin_max as max_coupon_margin, "+
                         " issuance_currency as currency, report_date as report_date ,active as active,instrument_id  from `dcs_v2_dcs_details` d "+
                         " left JOIN     ref_v2_dcs_coupon_map m "+
                         "   ON       m.coupon_code = d.coupon_code "+
                         "   JOIN     ref_v2_dcs_debt_map dm "+
                         "   ON       dm.debt_code = d.debt_code "+
                         " WHERE factset_entity_id = :entity ORDER BY `report_date` DESC)x "+
 
							" group by instrument_id,min_coupon_rate,max_coupon_rate,coupon_spread,max_coupon_margin,issue_amount,os_amount,maturity_date "+
 " order by active desc,report_date desc)y where  os_amount is not null)z join (select @id \\:=0) a ";*/
		
		String baseQuery=" select (@id \\:=@id+1) as id ,factset_entity_id,instrument_description,issue_date,coupon_type, coupon_index,asset_desc,collateral_type,issue_amount,null as maturity_year, "+
				" os_amount,avail_amount,instrument_type,maturity_date,min_coupon_rate,max_coupon_rate, coupon_spread, max_coupon_margin, issuance_currency, currency, report_date as report_date ,active as active,instrument_id  from (select * from (select * from(  select factset_entity_id,instrument_description,issue_date,coupon_desc as coupon_type, coupon_index,asset_desc,collateral_type,issue_amount, os_amount,  ( issue_amount - os_amount ) AS avail_amount, dm.debt_desc as instrument_type ,case "+
				" when maturity_year is null then null "+
				" when maturity_year_month is null then maturity_year "+
				" when maturity_year_day is null then concat(maturity_year,'-',lpad(maturity_year_month,2,0)) "+
				" else "+
				" concat(maturity_year, '-', lpad(maturity_year_month, 2, 0), '-', lpad( maturity_year_day, 2, 0)) end AS maturity_date,  coupon_rate_min  AS min_coupon_rate, coupon_rate_max  AS max_coupon_rate,  coupon_margin_min  AS coupon_spread,  coupon_margin_max as max_coupon_margin, issuance_currency, currency, report_date as report_date ,active as active,instrument_id  from `dcs_v2_dcs_details` d  left JOIN     ref_v2_dcs_coupon_map m    ON       m.coupon_code = d.coupon_code    JOIN     ref_v2_dcs_debt_map dm    ON dm.debt_code = d.debt_code  WHERE factset_entity_id = :entity ORDER BY `report_date` DESC)x  group by instrument_id,min_coupon_rate,max_coupon_rate,coupon_spread,max_coupon_margin,issue_amount,os_amount,maturity_date order by active desc,report_date desc)y where  os_amount is not null)z join (select @id \\:=0) a ";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(DebtCapitalStructureDetails.class);
		query.setParameter("entity", entityId);

		
		@SuppressWarnings("unchecked")
		List<DebtCapitalStructureDetails> data = (List<DebtCapitalStructureDetails>) query.list();
		System.out.println(data.size());

		List<DebtCapitalStructureDetailsDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, DebtCapitalStructureDetailsDTO.class);	

		return dcsDTOs;
		//return null;
	}

}
