# Smart Factory.Factory

---

## Functional Requirements

* [x] F1. The main entities we work with are factory, line (with priority), machine, man and product, 
material plus any other entities. Machines, people and products can be of different kinds.


* [x] F2.	Products are made in batches of a few hundred, if a batch of incompatible products changes, 
the production lines need to be rearranged. Each product has a defined sequence of equipment, robots, people that need to be arranged in a line.


* [x] F3. Machines and robots have consumption; people, robots, machines, and materials have a cost.


* [x] F4. Communication between machines, robots, and people is through events.
An event can receive 1 to N entities (human, machine, robot) that are registered for that kind of event. Events need to be checked in.


* [x] F5.	Individual devices have an API to collect data about that device. We collect data about devices such as electricity,
oil, material consumption and functionality (wear and tear increases with time)


* [x] F6. Machines and robots break down after a certain period of time. After breaking they generate an event (alert) 
with priority according to the importance of the line, which is handled by a human repairer.


* [x] F7.	There is a limited number of repairers. The repair takes several time units. 
An event is generated at the start of the repair and the end of the repair (will be useful for F10 request :-). Situations arise when no repairers are available - then one waits until one of them becomes available. When a repairer becomes available, the repairer starts on the 1st highest priority, 2nd oldest defect.


* [x] F8.	Visit of the director and inspector. We implement a factory visit, where the director walks through the factory 
exactly according to the tree hierarchy of entities factory ->* line -> *(machine|robot|person or product) and the inspector walks through according to the wear rate. Both the director and the inspector have actions defined on them to perform on a given entity type. Record the traversal sequence and the names of the actions performed in the log.


* [ ] F9.	The following reports must be generated for the factory for any time period:
  * FactoryConfigurationReport: all factory configuration data maintaining the hierarchy - factory ->* line -> *(machine|robot|man or product).
  * EventReport: what events were created in each period, where we group them (1) by event type, (2) by event source type, and (3) by who checked them in.
  * ConsumptionReport: how much each device, robot consumed electricity, oil, material. Including financial quantification. The report must also include summary consumption for parent entities (line|factory).
  * OuttagesReport: Longest outage, shortest outage, average outage time, average wait time for repairman and types of outage sources sorted by outage length.


* [ ] F10.	Return the states of the machines in the specified clock cycle (other than the last :-)). 
Reconstruct the state from the initial state and the sequence of events that were executed on the machine.




