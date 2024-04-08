package com.voltmoney.scheduler.services;

import com.voltmoney.scheduler.dao.Appointment;
import com.voltmoney.scheduler.dto.response.BaseResponse;
import com.voltmoney.scheduler.dto.response.OperatorAppointmentsResponse;
import com.voltmoney.scheduler.exceptions.BadRequest;
import com.voltmoney.scheduler.models.Slot;
import com.voltmoney.scheduler.repository.BookingRepository;
import com.voltmoney.scheduler.repository.ServiceOperatorRepository;
import com.voltmoney.scheduler.services.interfaces.ServiceOperator;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OperatorServiceImpl implements ServiceOperator {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public OperatorAppointmentsResponse bookedSlots(Long operatorId, LocalDate date) {
        validateOperator(operatorId);
        List<Appointment> appointments = bookingRepository.findByOperatorIdAndDate(operatorId, date);
        List<String> slots = appointments.stream().map(Appointment::getSlot).toList();
        List<Slot> slotList = new ArrayList<>();
        for (String slot : slots) {
            List<String> s = List.of(slot.split("-"));
            Integer startTime = Integer.valueOf(s.get(0));
            Integer endTime = Integer.valueOf(s.get(1));
            slotList.add(new Slot(date, startTime, endTime, operatorId));
        }
        return new OperatorAppointmentsResponse(operatorId, slotList);
    }

    private void validateOperator(Long operatorId){
        Optional<com.voltmoney.scheduler.dao.ServiceOperator> operator = serviceOperatorRepository.findById(operatorId);
        if(operator.isEmpty()){
            throw new BadRequest("OPERATOR_DOES_NOT_EXISTS");
        }

    }
    @Override
    public OperatorAppointmentsResponse openSlots(Long operatorId, LocalDate date) {
        validateOperator(operatorId);
        List<Appointment> appointments = bookingRepository.findByOperatorIdAndDate(operatorId, date);
        List<String> bookedSlots = appointments.stream()
                .map(Appointment::getSlot)
                .toList();
        if (bookedSlots.size() == 24) {
            return new OperatorAppointmentsResponse(operatorId, new ArrayList<>());
        }
        List<String> openSlots = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String block = i + "-" + (i + 1);
            if (!bookedSlots.contains(block)) {
                openSlots.add(block);
            }
        }

        List<List<Integer>> slots = new ArrayList<>();
        for (String s : openSlots) {
            List<String> l = Arrays.asList(s.split("-"));
            List<Integer> lSlot = Arrays.asList(Integer.valueOf(l.get(0)), Integer.valueOf(l.get(1)));
            slots.add(lSlot);
        }

        Stack<List<Integer>> result = new Stack<>();
        if (slots.size() == 1) {
            return new OperatorAppointmentsResponse(operatorId, List.of(new Slot(date, slots.get(0).get(0), slots.get(0).get(1), operatorId)));

        }
        result.push(slots.get(0));
        for (int i = 1; i < slots.size(); i++) {
            List<Integer> currentSlot = slots.get(i);
            if (currentSlot.get(0) <= result.peek().get(1)) {
                List<Integer> top = result.pop();
                result.push(Arrays.asList(Math.min(currentSlot.get(0), top.get(0)), Math.max(currentSlot.get(1), top.get(1))));
            } else {
                result.push(Arrays.asList(currentSlot.get(0), currentSlot.get(1)));
            }
        }

        List<List<Integer>> merged = new ArrayList<>();
        while (!result.isEmpty()) {
            merged.add(result.pop());
        }
        List<Slot> openSlotsMerged = new ArrayList<>();
        Collections.reverse(merged);
        for (List<Integer> ans : merged) {
            openSlotsMerged.add(new Slot(date, ans.get(0), ans.get(1), operatorId));
        }
        return new OperatorAppointmentsResponse(operatorId, openSlotsMerged);
    }

    public BaseResponse addOperator(com.voltmoney.scheduler.dao.ServiceOperator serviceOperator) {
        serviceOperatorRepository.save(serviceOperator);
        return new BaseResponse();
    }
}
