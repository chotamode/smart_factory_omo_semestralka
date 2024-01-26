# Projekt: Smart Factory

---

## Abstrakt

Vytvořit aplikaci pro virtuální simulaci inteligentní továrny, kde simulujeme chod výroby - na výrobních linkách s pomocí strojů a lidí vyrábíme produkty. Používáme jednotlivé stroje a vyhodnocujeme jejich využití, spotřebu a kvalitu výsledných výrobků. Součástí výrobního procesu jsou nejen stroje a lidé, ale i kolaborativní roboty. Základní časová jednotka je jeden takt (trvá jednu hodinu). Stavy továrny se mění (a vyhodnocují) po těchto taktech.

## Funkční požadavky

 - [x] **F1.** Hlavní entity se kterými pracujeme je továrna, linka (s prioritou), stroj, člověk a výrobek, materiál plus libovolné další entity. Stroje, lidé i výrobky mohou být různého druhu.  
 - [x] **F2.** Produkty se vyrábějí v sériích po několika stech kusů, jestliže se mění série nekompatibilních výrobků, tak je potřeba výrobní linky přeskládat. Každý výrobek má definovanou sekvenci zařízení, robotů, lidí, které je potřeba za sebou uspořádat na linku.  
 - [x] **F3.** Stroje a roboty mají svoji spotřebu; lidé, roboty, stroje i materiál stojí náklady.  
 - [x] **F4.** Komunikace mezi stroji, roboty a lidmi probíhá pomocí eventů. Event může dostat 1 až N entit (člověk, stroj, robot), které jsou na daný druh eventu zaregistrované. Eventy je potřeba odbavit.  
 - [x] **F5.** Jednotlivá zařízení mají API na sběr dat o tomto zařízení. O zařízeních sbíráme data jako je spotřeba elektřiny, oleje, materiálu a funkčnost (opotřebení roste s časem).  
 - [x] **F6.** Stroje a roboty se po určité době rozbijí. Po rozbití vygenerují event (alert) s prioritou podle důležitosti linky, který odbaví člověk - opravář.  
 - [x] **F7.** Opravářů je omezený počet. Oprava trvá několik taktů. Při začátku opravy a konci opravy je generován event (bude se hodit pro požadavek F10 :-). Vznikají situace, kdy nejsou dostupní žádní opraváři - pak se čeká dokud se některý z nich neuvolní. Po uvolnění opravář nastupuje na 1. nejprioritnější, 2. nejstarší defekt.  
 - [x] **F8.** Návštěva ředitele a inspektora. Realizujeme návštěvu továrny, kdy ředitel prochází továrnou přesně podle stromové hierarchie entit továrna ->* linka -> *(stroj|robot|člověk nebo výrobek) a inspektor prochází podle míry opotřebení. Ředitel i inspektor mají na sobě definované akce, které provedou s daným typem entity. Zapište sekvenci procházení a názvy provedených akcí do logu.  
 - [x] **F9.** Za továrnu je nutné vygenerovat následující reporty za libovolné časové období:  
    - FactoryConfigurationReport: veškerá konfigurační data továrny zachovávající hierarchii - továrna ->* linka -> *(stroj|robot|člověk nebo výrobek).  
    - EventReport: jaké za jednotlivé období vznikly eventy, kde je grupujeme 1) podle typu eventu, (2) podle typu zdroje eventu a (3) podle toho, kdo je odbavil.  
    - ConsumptionReport: Kolik jednotlivé zařízení, roboty spotřebovaly elektřiny, oleje, materiálu. Včetně finančního vyčíslení. V reportu musí být i summární spotřeby za nadřazané entity (linka|továrna)  
    - OuttagesReport: Nejdelší výpadek, nejkratší výpadek, průměrná doba výpadku, průměrná doba čekání na opraváře a typy zdrojů výpadků setříděné podle délky výpadku.  
 - [x] **F10.** Vraťte stavy strojů v zadaném taktu (jiném než posledním :-)). Stav zrekonstruujte z počátečního stavu a sekvence eventů, které byly na stroji provedeny.  

## Nefunkční požadavky

- [x] Není požadována autentizace ani autorizace  
- [x] Aplikace může běžet pouze v jedné JVM  
- [x] Aplikaci pište tak, aby byly dobře schované metody a proměnné, které nemají být dostupné ostatním třídám. Vygenerovný javadoc by měl mít co nejméně public metod a proměnných.  
- [x] Reporty jsou generovány do textového souboru  
- [x] Konfigurace továrny může být nahrávána přímo z třídy nebo externího souboru (preferován je json)  

## Požadované výstupy

- [x] Design ve formě use case diagramů, class diagramů a stručného popisu jak chcete úlohu realizovat  
- [x] Veřejné API - Javadoc vygenerovaný pro funkce, kterými uživatel pracuje s vaším software  
- [x] Dvě různé konfigurace továrny, alespoň 30 entit.  

## Spuštění

Pro spuštění aplikace je potřeba spustit třídu SmartFactory.java. V přikazové řádce bude vypsáno menu, ve kterém je možné vybrat jednu z možností.  

## Design patterny

### Factory

Factory Method pattern je použit v tomto projektu v rámci tříd pro vytváření reportů. Třída ReportCreator je abstraktní třída, která definuje metodu createReport(). Tato metoda je označena jako protected abstract, což znamená, že každá třída, která dědí z ReportCreator, musí implementovat svou vlastní verzi této metody.  

Konkrétní třídy jako ConfigurationReportCreator, ConsumptionReportCreator, EventReportCreator a OutagesReportCreator jsou konkrétní implementace ReportCreator. Každá z těchto tříd poskytuje svou vlastní implementaci metody createReport(), která vytváří specifický typ reportu.  Toto je klasický příklad Factory Method patternu, kde je definována abstraktní metoda pro vytváření objektu a konkrétní třídy poskytují implementaci pro vytváření konkrétních typů objektů.  

Tento návrhový vzor umožňuje flexibilitu při vytváření různých typů objektů bez nutnosti měnit kód, který tyto objekty vytváří.  

### Observer/Observable

Observer/Observable je návrhový vzor, který umožňuje objektům (tzv. pozorovatelům) sledovat změny stavu jiných objektů (tzv. pozorovaných). V tomto projektu byl tento vzor použit na dvou místech: pro reagování na události a pro simulaci času.  

* #### Reagování na události
V rámci reagování na události byl vzor Observer/Observable použit pro komunikaci mezi různými entitami v systému. Každá entita, která může generovat události (např. stroje, roboti), je pozorovaným objektem a každá entita, která může na tyto události reagovat (např. opraváři), je pozorovatelem.  Pozorované objekty generují události a publikují je na tzv. kanálech událostí (EventChannel). Pozorovatelé jsou na tyto kanály přihlášeni a když je na kanálu publikována nová událost, jsou o tom informováni a mohou na ni reagovat.  Tento mechanismus umožňuje efektivní a flexibilní komunikaci mezi entitami v systému bez nutnosti, aby měly tyto entity přímé odkazy na sebe navzájem.  

* #### Simulace času
Vzor Observer/Observable byl také použit pro simulaci času v systému. Třída TimeAndReportManager funguje jako pozorovaný objekt, který "tiká" čas v systému. Ostatní entity v systému, které potřebují reagovat na změny času, jsou pozorovatelé.  Když TimeAndReportManager "tikne" čas, informuje o tom všechny své pozorovatele. Ty pak mohou na tuto změnu času reagovat - například tím, že provedou nějakou akci nebo aktualizují svůj stav.  Tento mechanismus umožňuje synchronizovat chování všech entit v systému s časem a zároveň umožňuje flexibilní a efektivní simulaci času.

### Singleton
Singleton je návrhový vzor, který omezuje vytvoření třídy na jedinou instanci a poskytuje globální přístup k této instanci. Tento vzor byl v tomto projektu použit v třídě TimeAndReportManager.

Třída TimeAndReportManager je navržena tak, aby existovala pouze jedna instance této třídy v celém systému. Toto je dosaženo tím, že konstruktor třídy je soukromý, což znamená, že nelze vytvořit novou instanci třídy pomocí operátoru new. Místo toho je poskytnuta statická metoda getInstance(), která vrací jedinou existující instanci třídy.  Pokud ještě nebyla vytvořena žádná instance třídy TimeAndReportManager, metoda getInstance() ji vytvoří. Pokud již instance existuje, metoda getInstance() vrátí tuto existující instanci. Tímto způsobem je zajištěno, že v celém systému existuje vždy nejvýše jedna instance třídy TimeAndReportManager.  

Tento návrhový vzor je užitečný v situacích, kdy je potřeba, aby byla třída dostupná v celém systému, ale zároveň je důležité, aby byla zachována její jedinečnost. V tomto projektu je třída TimeAndReportManager zodpovědná za správu času, což je funkce, která by měla být v celém systému konzistentní a jedinečná.

### Visitor
Visitor je návrhový vzor, který umožňuje definovat novou operaci bez změny tříd objektů, na kterých operuje. Tento vzor byl v tomto projektu použit pro inspekci různých entit v systému.  

V tomto projektu je rozhraní Visitor definováno s metodami visit(), které přijímají různé typy objektů jako argumenty. Tyto metody jsou implementovány v konkrétních třídách, které implementují rozhraní Visitor, jako jsou Director a Inspector.  

Třída Director implementuje metody visit(), které jsou specifické pro návštěvu různých typů objektů v systému, jako jsou Factory, Machine, Cobot, Worker, Repairman, Product a ProductionLine. Podobně třída Inspector implementuje metody visit(), které jsou specifické pro inspekci různých typů objektů v systému.  

Rozhraní Visitable je definováno s metodou accept(), která přijímá objekt typu Visitor jako argument. Tato metoda je implementována v třídách, které implementují rozhraní Visitable. Když je metoda accept() volána na objektu, který implementuje rozhraní Visitable, předá se tento objekt jako argument do odpovídající metody visit() objektu Visitor.  

Tento návrhový vzor umožňuje definovat nové operace, které mohou být provedeny na objektech, aniž by bylo nutné měnit třídy těchto objektů. To umožňuje dodatečně přidávat nové funkce do systému bez nutnosti měnit existující kód.

### Builder
Builder je návrhový vzor, který odděluje konstrukci objektu od jeho reprezentace. Tento vzor byl v tomto projektu použit v třídě ProductBuilder.  

Třída ProductBuilder je navržena tak, aby poskytovala flexibilní a bezpečný způsob vytváření objektů třídy Product. Třída ProductBuilder obsahuje metody pro nastavení různých atributů objektu Product, jako jsou addProductMaterial() a addOperation(). Tyto metody vrací instanci ProductBuilder, což umožňuje řetězení volání metod pro vytváření objektu Product.  

Konečný objekt Product je vytvořen pomocí metody build(), která vrací vytvořený objekt Product. Tato metoda zajišťuje, že objekt Product je vytvořen až poté, co byly nastaveny všechny jeho atributy.  

Třída ProductBuilder také obsahuje metodu reset(), která resetuje stav builderu na jeho počáteční stav. To umožňuje opětovné použití instance ProductBuilder pro vytváření dalších objektů Product.  

Třída ProductBuilder také obsahuje metody pro vytváření předdefinovaných objektů Product, jako jsou buildProductSmartphone(), buildProductSmartWatch(), buildProductLaptop(), buildProductLearnTablet(), buildProductRemControlCar() a buildProductBlockSet(). Tyto metody vytvářejí objekty Product s předdefinovanými atributy.  

Tento návrhový vzor umožňuje vytvářet různé reprezentace objektu pomocí stejného kódu konstrukce. Tímto způsobem je možné vytvářet složité objekty krok za krokem a poskytovat uživateli jen ty metody, které jsou pro daný krok relevantní.

### Chain of responsibility
Chain of Responsibility je návrhový vzor, který umožňuje předat požadavky podél řetězce obslužných objektů. Když objekt přijme požadavek, rozhodne se, zda ho zpracuje, nebo ho předá dalšímu objektu v řetězci.  

V tomto projektu byl tento vzor použit v rámci výrobní linky. Každý pracovník (OperationalCapable) na výrobní lince má odkaz na dalšího pracovníka v řadě (nextWorker). Když pracovník dokončí svou operaci na produktu, předá produkt dalšímu pracovníkovi v řetězci.  

Tento mechanismus je implementován v metodě workOnProduct() třídy ProductionEntity. Když pracovník dokončí svou operaci na produktu, zkontroluje, zda existuje další pracovník v řetězci. Pokud ano, předá mu produkt k dalšímu zpracování. Pokud ne, označí produkt jako hotový.  

Tento návrhový vzor umožňuje flexibilní a efektivní zpracování požadavků v systému, kde může být požadavek zpracován několika různými objekty.

## UML diagramy

### Patterny

![Builder.png](documents%2Fdiagram_images%2Fpatterns%2FBuilder.png)
![Chain of responcibility.png](documents%2Fdiagram_images%2Fpatterns%2FChain%20of%20responcibility.png)
![Factory.png](documents%2Fdiagram_images%2Fpatterns%2FFactory.png)
![ObserverObservable, Singleton Time.png](documents%2Fdiagram_images%2Fpatterns%2FObserverObservable%2C%20Singleton%20Time.png)
![ObserverObservable Events.png](documents%2Fdiagram_images%2Fpatterns%2FObserverObservable%20Events.png)
![Visitor.png](documents%2Fdiagram_images%2Fpatterns%2FVisitor.png)

### Důležité třídy

![EventManagement.png](documents%2Fdiagram_images%2Fnecessery_classes%2FEventManagement.png)
![FactoryClass.png](documents%2Fdiagram_images%2Fnecessery_classes%2FFactoryClass.png)
![Management.png](documents%2Fdiagram_images%2Fnecessery_classes%2FManagement.png)

### Všechny třídy

![Class diagram v0.9.png](documents%2Fdiagram_images%2FClass%20diagram%20v0.9.png)