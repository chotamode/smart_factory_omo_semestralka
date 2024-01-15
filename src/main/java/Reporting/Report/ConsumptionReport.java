package Reporting.Report;

import Production.Factory.Factory;
import Production.ProductionEntity.Device.Device;
import Production.ProductionEntity.Device.Resourse.ConsumptionData;
import Production.ProductionLine.ProductionLine;
import Util.Constants;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates ConsumptionReport. ConsumptionReport contains information about consumption of Devices, ProductionLines and Factories.
 *
 *
 */
public class ConsumptionReport extends Report {

    Set<ConsumptionData> consumptionData;

    public ConsumptionReport(Long timeFrom, Long timeTo, Set<ConsumptionData> consumptionData) throws FileNotFoundException {
        super(timeFrom, timeTo);
        this.consumptionData = consumptionData;
    }

    @Override
    public void printReport() {

        Set<ConsumptionData> copyOfConsumptionData = new HashSet<>(consumptionData);

        Map<Device, List<ConsumptionData>> consumptionDataByDevice = copyOfConsumptionData.stream()
                .collect(Collectors.groupingBy(ConsumptionData::getConsumer));

        Map<Device, ConsumptionData> consumptionDataByDeviceFrom = new HashMap<>();
        Map<Device, ConsumptionData> consumptionDataByDeviceTo = new HashMap<>();

        for (Map.Entry<Device, List<ConsumptionData>> entry : consumptionDataByDevice.entrySet()) {
            Device device = entry.getKey();
            List<ConsumptionData> consumptionDataList = entry.getValue();

            ConsumptionData consumptionDataFrom = consumptionDataList.stream()
                    .filter(consumptionData -> consumptionData.getTimeStamp() >= getTimeFrom())
                    .min(Comparator.comparing(ConsumptionData::getTimeStamp))
                    .orElse(null);

            ConsumptionData consumptionDataTo = consumptionDataList.stream()
                    .filter(consumptionData -> consumptionData.getTimeStamp() <= getTimeTo())
                    .max(Comparator.comparing(ConsumptionData::getTimeStamp))
                    .orElse(null);

            consumptionDataByDeviceFrom.put(device, consumptionDataFrom);
            consumptionDataByDeviceTo.put(device, consumptionDataTo);
        }


        Map<ProductionLine, List<ConsumptionData>> consumptionDataByProductionLineFrom = new HashMap<>();
        Map<ProductionLine, List<ConsumptionData>> consumptionDataByProductionLineTo = new HashMap<>();
        filterByProductionLine(consumptionDataByDeviceFrom, consumptionDataByProductionLineFrom);
        filterByProductionLine(consumptionDataByDeviceTo, consumptionDataByProductionLineTo);

        Map<Factory, List<ConsumptionData>> consumptionDataByFactoryFrom = new HashMap<>();
        Map<Factory, List<ConsumptionData>> consumptionDataByFactoryTo = new HashMap<>();
        filterByFactory(consumptionDataByDeviceFrom, consumptionDataByFactoryFrom);
        filterByFactory(consumptionDataByDeviceTo, consumptionDataByFactoryTo);

        printConsumptionDataByDevice(consumptionDataByDeviceFrom, consumptionDataByDeviceTo);
        printConsumptionDataByProductionLine(consumptionDataByProductionLineFrom, consumptionDataByProductionLineTo);
        printConsumptionDataByFactory(consumptionDataByFactoryFrom, consumptionDataByFactoryTo);

    }

    private void printConsumptionDataByFactory(Map<Factory, List<ConsumptionData>> consumptionDataByFactoryFrom, Map<Factory, List<ConsumptionData>> consumptionDataByFactoryTo) {

        for (Map.Entry<Factory, List<ConsumptionData>> entry : consumptionDataByFactoryFrom.entrySet()) {
            Factory factory = entry.getKey();
            List<ConsumptionData> consumptionDataListFrom = entry.getValue();
            List<ConsumptionData> consumptionDataListTo = consumptionDataByFactoryTo.get(factory);

            printWriter.println("");
            printWriter.println("Factory " + factory.getName());
            printWriter.println("Oil: " + (consumptionDataListTo.stream().mapToInt(ConsumptionData::getConsumedOil).sum() - consumptionDataListFrom.stream().mapToInt(ConsumptionData::getConsumedOil).sum()) + ", cost: " + (consumptionDataListTo.stream().mapToInt(ConsumptionData::getConsumedOil).sum() - consumptionDataListFrom.stream().mapToInt(ConsumptionData::getConsumedOil).sum()) * Constants.OIL_COST);
            printWriter.println("Electricity: " + (consumptionDataListTo.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum() - consumptionDataListFrom.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum()) + ", cost: " + (consumptionDataListTo.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum() - consumptionDataListFrom.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum()) * Constants.ELECTRICITY_COST);
            printWriter.println();
        }

    }

    private void printConsumptionDataByProductionLine(Map<ProductionLine, List<ConsumptionData>> consumptionDataByProductionLineFrom, Map<ProductionLine, List<ConsumptionData>> consumptionDataByProductionLineTo) {

        for (Map.Entry<ProductionLine, List<ConsumptionData>> entry : consumptionDataByProductionLineFrom.entrySet()) {
            ProductionLine productionLine = entry.getKey();
            List<ConsumptionData> consumptionDataListFrom = entry.getValue();
            List<ConsumptionData> consumptionDataListTo = consumptionDataByProductionLineTo.get(productionLine);

            printWriter.println("");
            printWriter.println("Production line " + productionLine.getId());
            printWriter.println("Oil: " + (consumptionDataListTo.stream().mapToInt(ConsumptionData::getConsumedOil).sum() - consumptionDataListFrom.stream().mapToInt(ConsumptionData::getConsumedOil).sum()) + ", cost: " + (consumptionDataListTo.stream().mapToInt(ConsumptionData::getConsumedOil).sum() - consumptionDataListFrom.stream().mapToInt(ConsumptionData::getConsumedOil).sum()) * Constants.OIL_COST);
            printWriter.println("Electricity: " + (consumptionDataListTo.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum() - consumptionDataListFrom.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum()) + ", cost: " + (consumptionDataListTo.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum() - consumptionDataListFrom.stream().mapToLong(ConsumptionData::getConsumedElectricity).sum()) * Constants.ELECTRICITY_COST);
            printWriter.println();
        }

    }

    private void printConsumptionDataByDevice(Map<Device, ConsumptionData> consumptionDataByDeviceFrom, Map<Device, ConsumptionData> consumptionDataByDeviceTo) {

        for (Map.Entry<Device, ConsumptionData> entry : consumptionDataByDeviceFrom.entrySet()) {
            Device device = entry.getKey();
            ConsumptionData consumptionDataFrom = entry.getValue();
            ConsumptionData consumptionDataTo = consumptionDataByDeviceTo.get(device);

            printWriter.println("");
            printWriter.println("Device " + device.getName());
            printWriter.println("Oil: " + (consumptionDataTo.getConsumedOil() - consumptionDataFrom.getConsumedOil()) + ", cost: " + (consumptionDataTo.getConsumedOil() - consumptionDataFrom.getConsumedOil()) * Constants.OIL_COST);
            printWriter.println("Electricity: " + (consumptionDataTo.getConsumedElectricity() - consumptionDataFrom.getConsumedElectricity()) + ", cost: " + (consumptionDataTo.getConsumedElectricity() - consumptionDataFrom.getConsumedElectricity()) * Constants.ELECTRICITY_COST);
            printWriter.println();
        }

    }

    private void filterByFactory(Map<Device, ConsumptionData> consumptionDataByDeviceFrom, Map<Factory, List<ConsumptionData>> consumptionDataByFactoryFrom) {
        for (Map.Entry<Device, ConsumptionData> entry : consumptionDataByDeviceFrom.entrySet()) {
            ConsumptionData consumptionData = entry.getValue();
            Factory factory = consumptionData.getFactory();

            consumptionDataByFactoryFrom.computeIfAbsent(factory, k -> new ArrayList<>());
            consumptionDataByFactoryFrom.get(factory).add(consumptionData);
        }
    }

    private void filterByProductionLine(Map<Device, ConsumptionData> consumptionDataByDeviceFrom, Map<ProductionLine, List<ConsumptionData>> consumptionDataByProductionLineFrom) {
        for (Map.Entry<Device, ConsumptionData> entry : consumptionDataByDeviceFrom.entrySet()) {
            ConsumptionData consumptionData = entry.getValue();
            ProductionLine productionLine = consumptionData.getProductionLine();

            consumptionDataByProductionLineFrom.computeIfAbsent(productionLine, k -> new ArrayList<>());
            consumptionDataByProductionLineFrom.get(productionLine).add(consumptionData);
        }
    }
}
