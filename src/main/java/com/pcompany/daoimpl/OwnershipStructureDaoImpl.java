package com.pcompany.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dao.OwnershipStructureDao;
import com.pcompany.dto.OwnershipEmployeeJobDTO;
import com.pcompany.dto.OwnershipManagementInfoDTO;
import com.pcompany.dto.OwnershipPeopleInfoDTO;
import com.pcompany.entity.OwnershipEmployeeJob;
import com.pcompany.entity.OwnershipManagementInfo;
import com.pcompany.entity.OwnershipPeopleInfo;
import com.televisory.capitalmarket.util.DozerHelper;

@Repository
@Transactional
public class OwnershipStructureDaoImpl implements OwnershipStructureDao{
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<OwnershipManagementInfoDTO> getOwnershipManagementInfo(String entityId,String companyName) {
		
		/*String baseQuery = "SELECT (@id \\:= @id + 1) AS id,management,factset_person_id,designation,Date_format(appointed,'%d-%b-%Y') as appointed,other_companies, "+
			    " case "+
			    " when count > 0 then concat(experience,'+') "+
			    "  else  experience "+
			    "  end as experience, "+
			    "   min_inception_date,job_id,job_function_code,email,phone,biography,j_id,address, "+
			    "   fax,count "+
			    "FROM   ( SELECT   management,factset_person_id, "+
			    "                     group_concat(designation  SEPARATOR ', ') AS designation, "+
			    "                     appointed, "+
			    "                     group_concat(other_companies SEPARATOR '#:# ') AS other_companies, "+
			    "                     experience, "+
			    "                     min_inception_date, "+
			    "                     job_id, job_function_code, "+
			    "                     email, "+
			    "                     phone, "+
			    "                     biography, "+
			    "                     j_id, "+
			    "                     address, "+
			    "                     fax, "+
			    "                     count "+
			    "            FROM     ( "+
			    "                               SELECT p.full_name AS management, "+
			    "p.factset_person_id     AS factset_person_id, "+
			    "m.ppl_job_function_desc AS designation, "+
			    "j.inception_date        AS appointed, "+
			    "                                       (("+
			    "          SELECT group_concat(entity_proper_name SEPARATOR '#:# ') "+
			    "          FROM   `ff_v3_ff_od_jobs` jo "+
			    "          JOIN   `ff_v3_ff_od_job_functions` jf "+
			    "          ON     jo.job_id=jf.job_id "+
			    "          JOIN   ref_v2_ppl_job_function_map m "+
			    "          ON     jf.job_function_code=m.ppl_job_function_code "+
			    "          JOIN   sym_v1_sym_entity e "+
			    "          ON     e.factset_entity_id=jo.factset_entity_id "+
			    "          WHERE  jo.factset_person_id =j.factset_person_id "+
			    "          AND    m.ppl_job_function_desc LIKE 'Director%' "+
			    "          and e.entity_proper_name !=:companyName "+
			    "          AND    jf.termination_date IS NULL)) AS other_companies , "+
			    "                                         ( "+
			    "                                                SELECT TIMESTAMPDIFF(YEAR, "+
			    "                                                       ( "+
			    " SELECT   MIN(jo.inception_date) "+
			    " FROM     `ff_v3_ff_od_jobs` jo "+
			    " left JOIN     `ff_v3_ff_od_job_functions`f "+
			    " ON       jo.job_id=f.job_id "+
			    " WHERE    jo.factset_person_id=j.factset_person_id "+
			    " GROUP BY jo.factset_person_id),curdate())) AS experience, "+
			    "                                         ( SELECT MIN(jo.inception_date) "+
			    "                                                  FROM `ff_v3_ff_od_jobs` jo "+
			    "                                                  left JOIN `ff_v3_ff_od_job_functions`f "+
			    "                                                  ON jo.job_id=f.job_id "+
			    "                                                  WHERE jo.factset_person_id=j.factset_person_id "+
			    "                                                  GROUP BY jo.factset_person_id) AS min_inception_date, "+

			    "                                         j.job_id,jf.job_function_code,j.email AS email, "+
			    "                                         j.direct_telephone AS phone, "+
			    "                                         b.bio  AS biography, "+
			    "                                         jfc.id             AS j_id, "+
			    " concat(ifnull(a.location_street3,''),' ',ifnull(a.location_street2,''),' ',a.location_street1) AS address, "+
			    "                                         a.fax              AS fax, "+
			    "                                             (select count(*) from `ff_v3_ff_od_jobs` jo join `ff_v3_ff_od_job_functions` jf on jo.job_id=jf.job_id "+
			    " JOIN     ref_v2_ppl_job_function_map m ON jf.job_function_code=m.ppl_job_function_code join sym_v1_sym_entity e on e.factset_entity_id=jo.factset_entity_id where jo.factset_person_id =j.factset_person_id "+
			    " and jf.inception_date is null) as count "+
			    "                               FROM      `ff_v3_ff_od_people` p "+
			    "                               JOIN      `ff_v3_ff_od_jobs` j "+
			    "                               ON p.factset_person_id=j.factset_person_id "+
			    "                               JOIN `ff_v3_ff_od_job_functions` jf "+
			    "                               ON        j.job_id=jf.job_id "+
			    "                               JOIN ref_v2_ppl_job_function_map m "+
			    "                               ON jf.job_function_code=m.ppl_job_function_code "+
			    "                               JOIN      ff_v3_ff_od_bio b "+
			    "                               ON p.factset_person_id=b.factset_person_id "+
			    "                               JOIN `ent_v1_ent_entity_address` a "+
			    "                               ON a.address_id=j.address_id "+
			    "                               LEFT JOIN cm.job_function_code jfc "+
			    "                               ON jfc.job_function_code= jf.job_function_code "+
			    "                               WHERE j.factset_entity_id=:entityId "+
			    "                               AND       jf.active=1 "+
			    "                               AND       j.active=1 "+
			    "                               ORDER BY  jfc.id) y "+
			    "            GROUP BY factset_person_id "+
			    "            ORDER BY j_id)x "+
			    " JOIN "+
			    "   ( SELECT @id \\:= 0) AS ai ";*/
		
		String baseQuery= " SELECT (@id \\:= @id + 1) AS id, management,factset_person_id,designation,Date_format(appointed,'%d-%b-%Y') AS appointed,other_companies, "+ 
       " CASE "+
              " WHEN COUNT > 0 THEN concat(experience,'+') "+ 
              " ELSE experience  "+
       " END AS experience, "+ 
       " min_inception_date,email,phone,biography,address,fax, "+ 
       " COUNT "+ 
       " FROM   ( "+ 
                " SELECT   p.full_name                        AS management, "+ 
                         " p.factset_person_id                AS factset_person_id, "+ 
                         " group_concat(title SEPARATOR ', ') AS designation, "+ 
                         " CASE "+
                                 " WHEN title LIKE '%Chairman%' THEN 1 "+
                                 " WHEN title LIKE '%Founder%' THEN 2  "+
                                 " WHEN title LIKE '%Chief Executive Officer%' "+ 
                                 " OR       title LIKE '%CEO%' THEN 3  "+
                                 " WHEN title LIKE 'President%'  "+
                                 " OR       title LIKE '/President%' THEN 4 "+ 
                                 " WHEN title LIKE '%Chief Operating Officer%' THEN 5 "+ 
                                 " WHEN title LIKE '%Chief Administrative Officer%' THEN 6 "+ 
                                 " WHEN title LIKE '%Managing Director%' THEN 7  "+
                                 " WHEN title LIKE '%Director of Finance%'  "+
                                 " OR       title LIKE '%CFO%'  "+
                                 " OR       title LIKE '%Chief Financial Officer%' THEN 8 "+ 
                                 " WHEN title LIKE '%Chief Tech%'  "+
                                 " OR       title LIKE '%Sci%'  "+
                                 " OR       title LIKE '%R&D Officer%'THEN 9 "+ 
                                 " WHEN title LIKE '%Sales & Marketing%' THEN 10 "+ 
                                 " WHEN title LIKE '%Chief Investment Officer%' THEN 11 "+ 
                                 " WHEN title LIKE '%Director%'  "+
                                 " OR       title LIKE '%Board Member%' THEN 12 "+ 
                                 " WHEN title LIKE '%Independent Dir%' THEN 13  "+
                                 " WHEN title LIKE '%Assistant Managing Director%' THEN 14 "+ 
                                 " WHEN title LIKE '%Vice President%' THEN 15  "+
                                 " WHEN title LIKE '%VP%' THEN 16  "+
                                 " WHEN title LIKE '%Assistant Vice President%' "+ 
                                 " OR       title LIKE '%Assistant VP%' THEN 17  "+
                                 " ELSE 18 "+ 
                                 " END              AS d_order , "+ 
                                 " j.inception_date AS appointed, "+ 
                                 " ( "+ 
                         " ( "+ 
                               " SELECT group_concat(entity_proper_name SEPARATOR '#:# ') "+ 
                               " FROM   `ff_v3_ff_od_jobs` jo  "+
                               " JOIN   `ff_v3_ff_od_titles` t "+
                               " ON     jo.job_id=t.job_id  "+
                               " AND    jo.factset_person_id=t.factset_person_id "+ 
                               " JOIN   sym_v1_sym_entity e  "+
                               " ON     e.factset_entity_id=jo.factset_entity_id "+ 
                               " WHERE  t.factset_person_id =j.factset_person_id  "+
                               " AND    t.title LIKE '%Director%'  "+
                               " AND    t.active=1  "+
                               " AND    jo.active=1  "+
                               " AND    e.entity_proper_name !=:companyName "+ 
                               " AND    t.termination_date IS NULL)) AS other_companies , "+ 
                         " ( "+ 
                             "   SELECT TIMESTAMPDIFF(YEAR, "+ 
                                     "  ( "+ 
                                            "     SELECT    MIN(jo.inception_date) "+ 
                                                 " FROM      `ff_v3_ff_od_jobs` jo "+ 
                                                 " LEFT JOIN `ff_v3_ff_od_titles`t  "+
                                                 " ON        jo.job_id=t.job_id  "+
                                                 " AND       jo.factset_person_id=t.factset_person_id "+
                                                 " WHERE     jo.factset_person_id=j.factset_person_id "+
                                                 " GROUP BY  jo.factset_person_id),curdate())) AS experience, "+
                          " ( "+ 
                                 "  SELECT    MIN(jo.inception_date) "+ 
                                 "  FROM      `ff_v3_ff_od_jobs` jo  "+
                                 "  LEFT JOIN `ff_v3_ff_od_titles`t  "+
                                 "  ON        jo.job_id=t.job_id  "+
                                 "  AND       jo.factset_person_id=t.factset_person_id "+ 
                                 "  WHERE     jo.factset_person_id=j.factset_person_id  "+
                                 "  GROUP BY  jo.factset_person_id) AS min_inception_date, "+ 
                        " j.job_id, "+ 
                        " j.email                                                                                                  AS email, "+
                        " j.direct_telephone                                                                                      AS phone, "+
                        " b.bio                                                                                                   AS biography, "+
                         "         concat(ifnull(a.location_street3,''),' ',ifnull(a.location_street2,''),' ',a.location_street1) AS address, "+
                         " a.fax                                                                                                   AS fax, "+
                         " ( "+ 
                               " SELECT COUNT(*) "+ 
                               " FROM   `ff_v3_ff_od_jobs` jo "+ 
                               " JOIN   `ff_v3_ff_od_titles` t "+ 
                               " ON     jo.job_id=t.job_id "+ 
                               " AND    jo.factset_person_id=t.factset_person_id "+ 
                               " JOIN   sym_v1_sym_entity e "+ 
                               " ON     e.factset_entity_id=jo.factset_entity_id "+ 
                               " WHERE  t.factset_person_id =j.factset_person_id "+ 
                               " AND    t.inception_date IS NULL) AS COUNT  "+
                " FROM     `ff_v3_ff_od_people` p "+ 
                " JOIN     `ff_v3_ff_od_jobs` j "+
                " ON       p.factset_person_id=j.factset_person_id "+ 
                " JOIN     `ff_v3_ff_od_titles` t  "+
                " ON       j.job_id=t.job_id  "+
                " AND      j.factset_person_id=t.factset_person_id "+ 
                " JOIN     ff_v3_ff_od_bio b  "+
                " ON       p.factset_person_id=b.factset_person_id "+ 
                " JOIN     `ent_v1_ent_entity_address` a  "+
                " ON       a.address_id=j.address_id  "+
                " WHERE    j.factset_entity_id=:entityId "+ 
                " AND      t.active=1  "+
                " AND      j.active=1  "+
                " GROUP BY factset_person_id "+ 
                " ORDER BY d_order,  "+
                  "       title)x "+ 
                 " JOIN "+ 
      " ( "+ 
            "  SELECT @id \\:= 0) AS ai ";
		
		
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(OwnershipManagementInfo.class);
		query.setParameter("entityId", entityId);
		query.setParameter("companyName", companyName);
		
		
		@SuppressWarnings("unchecked")
		List<OwnershipManagementInfo> data = (List<OwnershipManagementInfo>) query.list();
		//System.out.println(data.size());

		List<OwnershipManagementInfoDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, OwnershipManagementInfoDTO.class);	
		
		return dcsDTOs;
	}

	@Override
	public List<OwnershipPeopleInfoDTO> getPeopleProfile(String personId) {
		String baseQuery="SELECT (@id \\:= @id + 1) as id,e.entity_proper_name as company,p.full_name,t.factset_person_id,"
				+ " t.title as position,concat(ifnull(a.location_street3,''),ifnull(a.location_street2,''),a.location_street1) as address,"
				+ " a.fax as fax,j.email,j.direct_telephone as phone,b.bio FROM `ff_v3_ff_od_titles` t"
				+ " join ff_v3_ff_od_bio b on t.factset_person_id=b.factset_person_id"
				+ " join `ff_v3_ff_od_people` p on t.factset_person_id=p.factset_person_id"
				+ " join `ff_v3_ff_od_jobs` j on j.factset_person_id=t.factset_person_id"
				+ " join `ent_v1_ent_entity_address` a on a.address_id=j.address_id"
				+ " join sym_v1_sym_entity e on e.factset_entity_id=j.factset_entity_id join"
				+" (SELECT @id \\:= 0) as ai where j.factset_person_id=:personId";
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(OwnershipPeopleInfo.class);
		query.setParameter("personId", personId);
		
		@SuppressWarnings("unchecked")
		List<OwnershipPeopleInfo> data = (List<OwnershipPeopleInfo>) query.list();
		System.out.println(data);

		List<OwnershipPeopleInfoDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, OwnershipPeopleInfoDTO.class);	
		
		return dcsDTOs;
	}

	@Override
	public List<OwnershipEmployeeJobDTO> getEmploymentHistory(String personId) {
		
		/*String baseQuery="SELECT (@id \\:= @id + 1) as id,company,title,period,experience,Date_format(termination_date,'%d-%b-%Y') AS termination_date, NULL AS NAME, NULL AS no_of_shares, NULL AS as_on_date, NULL AS percent_holdings from(select e.entity_proper_name as company,t.title as title,ifnull(t.inception_date,j.inception_date) as inception_date,concat(date_format(ifnull(t.inception_date,j.inception_date),'%b %Y'),'-',ifnull(date_format(ifnull(t.termination_date,j.termination_date),'%b %Y'),'-')) as period,(SELECT TIMESTAMPDIFF(YEAR,ifnull(t.inception_date,j.inception_date),ifnull(t.termination_date,j.termination_date))) as experience,ifnull(t.termination_date,j.termination_date) as termination_date FROM `ff_v3_ff_od_jobs` j left join `ff_v3_ff_od_titles` t on j.job_id=t.job_id "+
				" and j.factset_person_id=t.factset_person_id "+
				" join sym_v1_sym_entity e on e.factset_entity_id=j.factset_entity_id  where j.factset_person_id =:personId group by company,period "+
				" order by inception_date desc)x join (SELECT @id \\:= 0) as ai ";*/
		
		String baseQuery="SELECT (@id \\:= @id + 1) as id,company,title,Date_format(period,'%d-%b-%Y') AS period,experience,Date_format(termination_date,'%d-%b-%Y') as termination_date, NULL AS NAME, NULL AS no_of_shares, NULL AS as_on_date, NULL AS percent_holdings from(select e.entity_proper_name as company,t.title as title,ifnull(t.inception_date,j.inception_date) as period,(SELECT TIMESTAMPDIFF(YEAR,ifnull(t.inception_date,j.inception_date),ifnull(t.termination_date,j.termination_date))) as experience,ifnull(t.termination_date,j.termination_date) as termination_date FROM `ff_v3_ff_od_jobs` j left join `ff_v3_ff_od_titles` t on j.job_id=t.job_id "+
				" and j.factset_person_id=t.factset_person_id "+
				" join sym_v1_sym_entity e on e.factset_entity_id=j.factset_entity_id  where j.factset_person_id =:personId and t.title is not null and t.title != '' "+ 
				" group by company,period "+
				" order by period desc)x join (SELECT @id \\:= 0) as ai";
		
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(OwnershipEmployeeJob.class);
		query.setParameter("personId", personId);
		
		@SuppressWarnings("unchecked")
		List<OwnershipEmployeeJob> data = (List<OwnershipEmployeeJob>) query.list();
		System.out.println(data);

		List<OwnershipEmployeeJobDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, OwnershipEmployeeJobDTO.class);	
		
		return dcsDTOs;
	}

	@Override
	public List<OwnershipEmployeeJobDTO> getDirectorship(String personId) {
		
		/*String baseQuery="SELECT (@id \\:= @id + 1) as id,factset_person_id,company,title,period,experience,title_active,job_active, NULL AS termination_date, NULL as name, NULL as no_of_shares, NULL as as_on_date, NULL as percent_holdings from (select j.factset_person_id,entity_proper_name as company,t.title as title,concat(date_format(t.inception_date,'%b %Y'),'-',ifnull(date_format(t.termination_date,'%b %Y'),'-')) as period,(SELECT TIMESTAMPDIFF(YEAR,t.inception_date,t.termination_date)) as experience,t.active as title_active,j.active as job_active FROM   `ff_v3_ff_od_jobs` j "+
               " JOIN   `ff_v3_ff_od_titles` t ON     j.job_id=t.job_id and j.factset_person_id=t.factset_person_id "+
               " JOIN   sym_v1_sym_entity e ON     e.factset_entity_id=j.factset_entity_id "+
              " WHERE  j.factset_person_id =:personId AND    t.title LIKE '%Director%' group by company,period,t.title "+
              " order by t.inception_date desc) x join (SELECT @id \\:= 0) as ai ";*/
		
		String baseQuery="SELECT (@id \\:= @id + 1) as id,factset_person_id,company,title,Date_format(period,'%d-%b-%Y') AS period,experience,title_active,job_active,Date_format(termination_date,'%d-%b-%Y') AS termination_date, NULL as name, NULL as no_of_shares, NULL as as_on_date, NULL as percent_holdings  from (select j.factset_person_id,entity_proper_name as company,t.title as title,ifnull(t.inception_date,j.inception_date) as period,(SELECT TIMESTAMPDIFF(YEAR,t.inception_date,t.termination_date)) as experience,t.active as title_active,j.active as job_active,ifnull(t.termination_date,j.termination_date) as termination_date FROM   `ff_v3_ff_od_jobs` j "+
              " JOIN   `ff_v3_ff_od_titles` t ON     j.job_id=t.job_id and j.factset_person_id=t.factset_person_id "+
              " JOIN   sym_v1_sym_entity e ON     e.factset_entity_id=j.factset_entity_id "+
              " WHERE  j.factset_person_id =:personId AND    t.title LIKE '%Director%' "+
              " group by company,period,t.title "+
              " order by t.inception_date desc) x join (SELECT @id \\:= 0) as ai";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(OwnershipEmployeeJob.class);
		query.setParameter("personId", personId);
		
		@SuppressWarnings("unchecked")
		List<OwnershipEmployeeJob> data = (List<OwnershipEmployeeJob>) query.list();
		System.out.println(data);

		List<OwnershipEmployeeJobDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, OwnershipEmployeeJobDTO.class);	
		
		return dcsDTOs;
	}

	@Override
	public List<OwnershipEmployeeJobDTO> getPeopleHoldings(String companyId, String personId) {
		//String baseQuery="SELECT (@row_number \\:= @row_number+1) as id, e.entity_proper_name as company,t.title as title,ocp.full_name as name,reported_shares_owned as no_of_shares,tran_date as as_on_date , NULL as period, NULL as experience FROM own_v5_own_con_jobs ocj inner join own_v5_own_con_people ocp on ocp.factset_person_id=ocj.factset_person_id inner join own_v5_own_con_titles t on t.factset_person_id=ocp.factset_person_id inner join `own_v5_own_insider_trans` it on it.factset_entity_id=ocj.factset_entity_id join sym_v1_sym_entity e on it.factset_entity_id=e.factset_entity_id join (select @row_number \\:=0) as t where ocj.factset_entity_id=:entityId group by year(as_on_date)";
		
		/*String baseQuery="select (@row_number \\:=@row_number + 1) as id,company_name as company ,title,name,no_of_shares,as_on as as_on_date,"+
				" percent_holdings,NULL AS termination_date, NULL AS period, NULL AS experience  from (select  e.entity_proper_name as company_name, t.title as title,p.full_name as name,it.reported_shares_owned as no_of_shares, "+
				" it.effective_date as as_on,it.factset_entity_id as factset_entity_id, "+
		" (it.reported_shares_owned/(ff_com_shs_out*1000000))*100 as percent_holdings from `own_v5_own_insider_trans` it  join cm.company_list c on it.fsym_id=c.security_id "+
		" join ff_v3_ff_od_jobs j  on j.factset_entity_id=c.factset_entity_id "+
		" join ff_v3_ff_od_people p on j.factset_person_id=p.factset_person_id "+
		" join ff_v3_ff_od_titles t on t.factset_person_id=p.factset_person_id "+
		" join sym_v1_sym_entity e on it.factset_entity_id=e.factset_entity_id "+
		" join factset.ff_v3_ff_basic_af b on b.fsym_id=c.company_id and it.effective_date=b.date "+
		" where it.fsym_id=:companyId and t.factset_person_id=:personId and "+
		" effective_date >= date_sub(curdate(),interval 5 year) "+
		" group by year(as_on),it.factset_entity_id) x join "+
		" (SELECT @row_number \\:=0) AS t "+
		" order by percent_holdings ";*/
		
		
		String baseQuery="select (@row_number \\:=@row_number + 1) AS id,fsym_id, factset_entity_id,total_shares, shares_owned as no_of_shares,Date_format(as_on,'%d-%b-%Y')  as as_on_date, name,title, company_id,company_name as company, "+
" percent_holdings,NULL AS termination_date, NULL AS period, NULL AS experience from (select it.fsym_id as fsym_id, it.factset_entity_id as factset_entity_id,b.ff_com_shs_out*1000000 as total_shares,it.position as shares_owned,report_date as as_on,p.full_name as name,t.title as title,c.company_id as company_id,c.proper_name as company_name, "+
" (it.position/(b.ff_com_shs_out*1000000))*100 as percent_holdings "+
" from `own_v5_own_stakes_detail` it join cm.company_list c "+
" on c.security_id=it.fsym_id "+
" join factset.ff_v3_ff_basic_cf b on b.fsym_id=c.company_id "+
" join ff_v3_ff_od_people p  on p.factset_person_id=it.factset_entity_id "+
" join ff_v3_ff_od_titles t on t.factset_person_id=p.factset_person_id "+
" join sym_v1_sym_entity e on it.factset_entity_id=e.factset_entity_id "+
" where it.factset_entity_id=:personId and it.current_flag=1 "+
" order by c.domicile_flag desc)x JOIN ( SELECT @row_number \\:=0) AS t group by fsym_id ";
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(OwnershipEmployeeJob.class);
		
		query.setParameter("personId", personId);
		
		@SuppressWarnings("unchecked")
		List<OwnershipEmployeeJob> data = (List<OwnershipEmployeeJob>) query.list();
		System.out.println(data);

		List<OwnershipEmployeeJobDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, OwnershipEmployeeJobDTO.class);	
		
		return dcsDTOs;
	}

}
