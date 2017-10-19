public without sharing class CS_CompanyInformationPDFController_Long{
    
    
    private List<id> lstAccountDisplayId;
    private List<Opportunity> lstOpportunityDisplay;
    private List<AccountContactRelation> lstAccountContactRelationDisplay;
    private List<Event> lstMeeting;
    private list<CampaignMember> lstCampaignMember;
    private map<id, List<Opportunity>> mapAccountOpportunity;
    
    public String ultimateParent{get;set;}
    public list<CampaignMember> lstCampaignEvent{get;set;}
    public list<CampaignMember> lstCampaignPublication{get;set;}
    public list<CampaignMember> lstCampaignVIP{get;set;}
    public List<MeetingWrapper> lstMeetingWrapper{get;set;}
    public set<String> setDept{get;set;}
    public integer deptNo{get;set;}
    public map<String,integer> mapDeptJObNo{get;set;}
    public List<Account> lstAccountDisplay{get;set;}
    public List<Who_Knows_Who__c> lstWhoKnowsWho{get; set;}
    public List<OpportunityWrapper> lstOpportunityWrapperDisplay{get;set;}
    public List<ContactWrapper> lstContactWrapperDisplay{get;set;}
    /**
    *  Method Name: CS_CompanyInformationPDFController
    *  Description: Constructor for CS_CompanyInformationPDFController
    *  Param:  None
    *  Return: None
    */
    public CS_CompanyInformationPDFController_Long(ApexPages.StandardController stdController) {
        
        
        Account objAccount = new Account();
        set<id> setAccountIds = new set<id>();
        set<id> setContactIds = new set<id>();
        lstAccountDisplay = new List<Account>();
        List<Account> lstAccount = new List<Account>();
        List<Contact> lstContact = new List<Contact>();
        lstOpportunityDisplay = new List<Opportunity>();
        
        
        mapAccountOpportunity = new map<id, List<Opportunity>>();
        lstCampaignEvent = new list<CampaignMember>();
        lstCampaignPublication = new list<CampaignMember>();
        lstCampaignVIP = new list<CampaignMember>();
        lstCampaignMember  = new list<CampaignMember>();
        lstMeetingWrapper = new List<MeetingWrapper>();
        lstWhoKnowsWho = new List<Who_Knows_Who__c>();
        lstMeeting = new List<Event>();
        deptNo = 0;
        ultimateParent ='N/A';
        setDept = new set<String>();
        mapDeptJObNo = new map<String,integer>();
        lstAccountDisplayId = new List<id>();
        lstOpportunityWrapperDisplay = new List<OpportunityWrapper>();
        lstContactWrapperDisplay = new List<ContactWrapper>();
        lstAccountContactRelationDisplay = new List<AccountContactRelation>();
        
        if(stdController != null){
            objAccount = (Account)stdController.getRecord();
            integer noOFIterations = 1;
            List<Account> lstAccountParent = new List<Account>();
            if(!String.isBlank(objAccount.ultimateParentId__c)){
                lstAccountParent = [Select id,name
                                            From
                                            Account 
                                            where id =:objAccount.ultimateParentId__c limit 1];
                if(!lstAccountParent.isEmpty()){
                    ultimateParent = lstAccountParent[0].Name;
                }
            }
            
            setAccountIds.add(objAccount.id);
            while(!setAccountIds.isEmpty()){
                lstAccount = new List<Account>();
                if(noOFIterations == 1){
                    lstAccount =[Select id, name,
                                            ParentId,
                                            Parent.Name,
                                            Parent.ParentId,
                                            Parent.Parent.Name, 
                                            Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Name,
                                            Parent.Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Parent.Name,
                                            Parent.Parent.Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Parent.Parent.Name
                                            From 
                                            Account
                                            where 
                                            id in: setAccountIds
                                            or ParentId in: setAccountIds
                                            or ParentId in: setAccountIds
                                            or Parent.ParentId in: setAccountIds
                                            or Parent.Parent.ParentId in: setAccountIds
                                            or Parent.Parent.Parent.ParentId in: setAccountIds
                                            or Parent.Parent.Parent.Parent.ParentId in: setAccountIds
                                            order by Parent.Parent.Parent.Parent.Parent.Name, Parent.Parent.Parent.Parent.Name, Parent.Parent.Parent.Name,
                                            Parent.Parent.Name, Parent.Name, Name Limit 100];
                                        
               
                }else{
                    lstAccount =[Select id, name,
                                            ParentId,
                                            Parent.Name,
                                            Parent.ParentId,
                                            Parent.Parent.Name, 
                                            Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Name,
                                            Parent.Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Parent.Name,
                                            Parent.Parent.Parent.Parent.ParentId,
                                            Parent.Parent.Parent.Parent.Parent.Name
                                            From 
                                            Account
                                            where 
                                            ParentId in: setAccountIds
                                            or ParentId in: setAccountIds
                                            or Parent.ParentId in: setAccountIds
                                            or Parent.Parent.ParentId in: setAccountIds
                                            or Parent.Parent.Parent.ParentId in: setAccountIds
                                            or Parent.Parent.Parent.Parent.ParentId in: setAccountIds
                                            order by Parent.Parent.Parent.Parent.Parent.Name, Parent.Parent.Parent.Parent.Name, Parent.Parent.Parent.Name,
                                            Parent.Parent.Name, Parent.Name, Name Limit 100];
                    
                }
                                        
                setAccountIds = new set<id>();                     
                for(Account objAccountTopParent: lstAccount){
                    if(objAccountTopParent.Parent.Parent.Parent.Parent.Parentid != null){
                        setAccountIds.add(objAccountTopParent.id);
                    }
                }
                lstAccountDisplay.addAll(lstAccount);
                
                if(noOFIterations >= 10){
                    break;
                }
                noOFIterations = noOFIterations+1;
                if(setAccountIds.size() == 0){
                    break;
                }
            }
            
            for(Account objAccountFinal: lstAccountDisplay){
                lstAccountDisplayId.add(objAccountFinal.id);
                mapAccountOpportunity.put(objAccountFinal.id, new List<Opportunity>());
            }
            
            System.debug('----->'+lstAccountDisplayId);
            
            if(!lstAccountDisplayId.isEmpty()){
                lstOpportunityDisplay = [Select id, name,
                                                AccountId,
                                                Account.Name,
                                                Date_Instructed__c,
                                                StageName,
                                                Manager__c,
                                                Manager__r.Name,
                                                Manager__r.Department__c,
                                                Manager__r.Office__c,
                                                Department__c,
                                                Office__c,
                                                Work_Type__c,
                                                Job_Number__c,
                                                (select id,name,
                                                Property__c,
                                                Property__r.Suite_Unit__c,
                                                Property__r.Floor_No__c,
                                                Property__r.Building_Name__c,
                                                Property__r.Estate__c,
                                                Property__r.Street_No__c,
                                                Property__r.Street__c,
                                                Property__r.Area__c,
                                                Property__r.Town__c,
                                                Property__r.County__c,
                                                Property__r.Post_Code__c,
                                                Property__r.Country__c
                                                From
                                                Job_Property_Junction__r)
                                                From Opportunity
                                                where AccountId in: lstAccountDisplayId
                                                and StageName = 'Instructed'
                                                and CloseDate >= LAST_FISCAL_YEAR
                                                order by Date_Instructed__c desc Limit 1000];
                                                
                lstAccountContactRelationDisplay = [Select id,
                                                        ContactId,
                                                        Contact.Name,
                                                        Contact.Job_Title__c,
                                                        Contact.Title,
                                                        AccountId
                                                        From
                                                        AccountContactRelation
                                                        where 
                                                        AccountId in:lstAccountDisplayId Limit 1000];
            }
            System.debug('----->'+lstOpportunityDisplay);
            
            for(Opportunity objOppDisplayFinal: lstOpportunityDisplay){
                System.debug('----->'+objOppDisplayFinal.id);
                mapAccountOpportunity.get(objOppDisplayFinal.AccountId).add(objOppDisplayFinal);
                
            }
            
            for(id idAccountId: mapAccountOpportunity.keyset()){
                
                for(Opportunity objOppDisplay: mapAccountOpportunity.get(idAccountId)){
                    deptNo +=1;
                    if(!String.isBlank(objOppDisplay.Manager__r.Department__c)){
                        setDept.add(objOppDisplay.Manager__r.Department__c);
                        if(mapDeptJObNo.containsKey(objOppDisplay.Manager__r.Department__c)){
                            integer currentNo = mapDeptJObNo.get(objOppDisplay.Manager__r.Department__c);
                            currentNo += 1;
                            mapDeptJObNo.put(objOppDisplay.Manager__r.Department__c, currentNo);
                        }else{
                            mapDeptJObNo.put(objOppDisplay.Manager__r.Department__c, 1);
                        }
                        
                    }else{
                        
                        if(mapDeptJObNo.containsKey('None')){
                            integer currentNone = mapDeptJObNo.get('None');
                            currentNone += 1;
                            mapDeptJObNo.put('None', currentNone);
                        }else{
                            mapDeptJObNo.put('None', 1);
                        }
                    }
                    String propAdd = 'None';
                    if(!objOppDisplay.Job_Property_Junction__r.isEmpty()){
                        if(objOppDisplay.Job_Property_Junction__r.size() > 1){
                            propAdd = 'Multiple';
                        }else{
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Suite_Unit__c))
                                propAdd = objOppDisplay.Job_Property_Junction__r[0].Property__r.Suite_Unit__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Floor_No__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Floor_No__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Building_Name__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Building_Name__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Estate__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Estate__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Street_No__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Street_No__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Street__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Street__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Area__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Area__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Town__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Town__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.County__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.County__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Post_Code__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Post_Code__c+' ';
                            if(!String.isBlank(objOppDisplay.Job_Property_Junction__r[0].Property__r.Country__c))
                                propAdd +=objOppDisplay.Job_Property_Junction__r[0].Property__r.Country__c;
                        }
                    }
                    OpportunityWrapper objOpportunityWrapper = new OpportunityWrapper(objOppDisplay, propAdd);
                    lstOpportunityWrapperDisplay.add(objOpportunityWrapper);
                }
                
            }
            
            if(mapDeptJObNo.containsKey('None')){
                setDept.add('None');
            }
            for(AccountContactRelation objAccountContactRelation: lstAccountContactRelationDisplay){
                setContactIds.add(objAccountContactRelation.ContactId);
            }
            if(!setContactIds.isEmpty()){
                lstContact =[Select id, name,
                                    Job_Title__c,
                                    Title,
                                    (select id, 
                                    Contact__c,
                                    Contact__r.Name,
                                    Contact__r.Job_Title__c,
                                    Contact__r.Title,
                                    Personal_Relationship__c,
                                    Strength_of_Relationship__c,
                                    Relationship_Strength__c,
                                    Relationship_Strength_Num__c,
                                    Staff__c,
                                    Staff__r.Name
                                    From Who_Knows_Who__r)
                                    From 
                                    Contact 
                                    where id in: setContactIds order by Name Limit 1000];
                

                lstWhoKnowsWho = [Select id, 
                                        Contact__c,
                                        Contact__r.Name,
                                        Contact__r.Job_Title__c,
                                        Contact__r.Title,
                                        Personal_Relationship__c,
                                        Strength_of_Relationship__c,
                                        Relationship_Strength__c,
                                        Relationship_Strength_Num__c,
                                        Staff__c,
                                        Staff__r.Name
                                        From Who_Knows_Who__c
                                        where Contact__c in: setContactIds order by Relationship_Strength_Num__c, Staff__r.Name Limit 500];
                map<id, Contact> mapIDContact = new map<id, Contact>(); 
                map<id, String> mapIDWhoknowsWho = new  map<id, String>();                      
                for(Contact objContactDisplay: lstContact){
                    String whoknow= '';
                    if(!objContactDisplay.Who_Knows_Who__r.isEmpty()){
                        // lstWhoKnowsWho.addAll(objContactDisplay.Who_Knows_Who__r);
                        for(Who_Knows_Who__c objWhoKnows: objContactDisplay.Who_Knows_Who__r){
                            if(!String.isBlank(objWhoKnows.Staff__r.Name)){
                                whoknow+= objWhoKnows.Staff__r.Name+ ', ';
                            }
                        }
                        whoknow = whoknow.substringBeforeLast(',');
                        mapIDWhoknowsWho.put(objContactDisplay.id, whoknow);
                    }else{
                        whoknow ='None';
                        mapIDWhoknowsWho.put(objContactDisplay.id, whoknow);
                    }
                    mapIDContact.put(objContactDisplay.id, objContactDisplay);
                    ContactWrapper objContactWrapper = new ContactWrapper(objContactDisplay, whoknow);
                    lstContactWrapperDisplay.add(objContactWrapper);
                }
                
                
                lstMeeting = [Select id, whatId,
                                    WhoId,
                                    ActivityDate
                                    From
                                    Event 
                                    where WhoId in:mapIDContact.keyset() order by ActivityDate desc Limit 500 ];
                
                for(Event objEvent: lstMeeting){
                    if(objEvent.WhoId != null){
                        String strWho = '';
                        String strName = '';
                        String strTitle = '';
                        if(mapIDWhoknowsWho.containsKey(objEvent.WhoId)){
                            strWho = mapIDWhoknowsWho.get(objEvent.WhoId);
                        }
                        if(mapIDContact.containsKey(objEvent.WhoId)){
                            Contact objContact = mapIDContact.get(objEvent.WhoId);
                            If(!String.isBlank(objContact.name)){
                                strName = objContact.name;
                            }
                            If(!String.isBlank(objContact.Title)){
                                strTitle = objContact.Title;
                            }
                        }
                        MeetingWrapper objMeetingWrapper = new MeetingWrapper(objEvent, strWho, strName, strTitle);
                        lstMeetingWrapper.add(objMeetingWrapper);
                    }
                }
                                        
                
                lstCampaignEvent = [Select id, name,
                                            Attended_Status__c,
                                            Status,
                                            Contact_Event_Score__c,
                                            Invited_By__c,
                                            Invited_By__r.Name,
                                            Invited_How__c,
                                            CampaignId,
                                            Campaign.Event_Date__c,
                                            Campaign.Publication_Date__c,
                                            Campaign.Name,
                                            Campaign.RecordType.Name,
                                            Campaign.RecordType.DeveloperName,
                                            Campaign.RecordTypeId,
                                            Campaign.Publication_Type__c,
                                            Campaign.Geographies__c,
                                            Campaign.Sectors__c,
                                            ContactId,
                                            Contact.Name,
                                            Contact.Job_Title__c,
                                            Contact.Title,
                                            Contact.AccountId,
                                            Contact.Account.Name
                                            From
                                            CampaignMember
                                            where ContactId in:setContactIds 
                                            and Campaign.RecordType.DeveloperName = 'Event'
                                            order by Campaign.Event_Date__c desc, Contact.Name limit 500];
                                            
                lstCampaignPublication = [Select id, name,
                                            Attended_Status__c,
                                            Status,
                                            Contact_Event_Score__c,
                                            Invited_By__c,
                                            Invited_By__r.Name,
                                            Invited_How__c,
                                            CampaignId,
                                            Campaign.Event_Date__c,
                                            Campaign.Publication_Date__c,
                                            Campaign.Name,
                                            Campaign.RecordType.Name,
                                            Campaign.RecordType.DeveloperName,
                                            Campaign.RecordTypeId,
                                            Campaign.Publication_Type__c,
                                            Campaign.Geographies__c,
                                            Campaign.Sectors__c,
                                            ContactId,
                                            Contact.Name,
                                            Contact.Job_Title__c,
                                            Contact.Title,
                                            Contact.AccountId,
                                            Contact.Account.Name
                                            From
                                            CampaignMember
                                            where ContactId in:setContactIds 
                                            and Campaign.RecordType.DeveloperName = 'Publication'
                                            order by Campaign.Publication_Date__c desc, Contact.Name limit 500];
                                            
                lstCampaignVIP = [Select id, name,
                                        Attended_Status__c,
                                        Status,
                                        Contact_Event_Score__c,
                                        Invited_By__c,
                                        Invited_By__r.Name,
                                        Invited_How__c,
                                        CampaignId,
                                        Campaign.Event_Date__c,
                                        Campaign.Publication_Date__c,
                                        Campaign.Name,
                                        Campaign.RecordType.Name,
                                        Campaign.RecordType.DeveloperName,
                                        Campaign.RecordTypeId,
                                        Campaign.Publication_Type__c,
                                        Campaign.Geographies__c,
                                        Campaign.Sectors__c,
                                        ContactId,
                                        Contact.Name,
                                        Contact.Job_Title__c,
                                        Contact.Title,
                                        Contact.AccountId,
                                        Contact.Account.Name
                                        From
                                        CampaignMember
                                        where ContactId in:setContactIds 
                                        and Campaign.RecordType.DeveloperName = 'VIP_List'
                                        order by Contact.Name limit 500];
                                            
                // for(CampaignMember objCampaignMember: lstCampaignMember){
                    
                    // if(objCampaignMember.Campaign.RecordTypeId != null){
                        // if(objCampaignMember.Campaign.RecordType.DeveloperName.equalsIgnoreCase('Event')){
                            // lstCampaignEvent.add(objCampaignMember);
                        // }else if(objCampaignMember.Campaign.RecordType.DeveloperName.equalsIgnoreCase('Publication')){
                            // lstCampaignPublication.add(objCampaignMember);
                        // }else if(objCampaignMember.Campaign.RecordType.DeveloperName.equalsIgnoreCase('VIP_List')){
                            // lstCampaignVIP.add(objCampaignMember);
                        // }
                    // }
                // }
            }
            
        }
        
    }
    
    public class OpportunityWrapper{
        public Opportunity objOpportunityToDisplay{get;set;}
        public String propertyaddress{get;set;}
        public OpportunityWrapper(Opportunity objOpp, String propAddress){
            propertyaddress = propAddress;
            objOpportunityToDisplay =  objOpp;
        }
    }
    
    public class ContactWrapper{
        public Contact objContactToDisplay{get;set;}
        public String whoknowswho{get;set;}
        public ContactWrapper(Contact objCon, String whoknow){
            whoknowswho = whoknow;
            objContactToDisplay =  objCon;
        }
    }
    
    public class MeetingWrapper{
        public Event objEventToDisplay{get;set;}
        public String whoknowswho{get;set;}
        public String contactName{get;set;}
        public String contactJobTitle{get;set;}
        public MeetingWrapper(Event objEventTo, String whoknow, String contactNameJob,String contactJobTitleJob){
            objEventToDisplay =objEventTo;
            whoknowswho = whoknow;
            contactName = contactNameJob;
            contactJobTitle = contactJobTitleJob;
        }
    }
    
    
}
