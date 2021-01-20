import React from 'react';
import {CashSaleToPay} from "./CashSaleToPay";
import {cleanup, fireEvent, render, screen} from '@testing-library/react';

describe('CashSaleToPay', () => {

    let mockProps = {
        saleId: 3,
        totalPayment: 100,
        minPayment: 5,
        maxPayment: 150,
        editableAmount: true,
        successCallback: jest.fn()
    };

    afterEach(() => {
        cleanup();
    });

    it('should render saleId', () => {
        render(<CashSaleToPay {...mockProps} />);
        const label = screen.getByText(/Venta/);

        expect(label).toHaveTextContent("Venta: " + mockProps.saleId);
    });

    it('should change toggle button on click', () => {
        render(<CashSaleToPay {...mockProps} />);
        const label = screen.getByText(/No Confirmado/);

        fireEvent.click(label);

        expect(label).toHaveTextContent("Confirmado");
        expect(mockProps.successCallback).toHaveBeenCalledWith(3, 100);
    });

    it('should change not toggle button on second click', () => {
        render(<CashSaleToPay {...mockProps} />);
        const toggleButton = screen.getByText(/No Confirmado/);

        fireEvent.click(toggleButton);

        expect(toggleButton).toHaveTextContent("Confirmado");
        expect(mockProps.successCallback).toHaveBeenCalledWith(3, 100);

        fireEvent.click(toggleButton);
        expect(toggleButton).toHaveTextContent("Confirmado");

    });

    it('should change input when the amount is editable', () => {
        render(<CashSaleToPay {...mockProps}/>);

        const input = screen.getByRole("textbox")

        fireEvent.change(input, {target: {value: "120"}})

        expect(input).toHaveValue("120");
    });

    it('should not change input when editableAmount is false', () => {
        const noEditableProps = {...mockProps, ...{editableAmount: false}};

        render(<CashSaleToPay {...noEditableProps}/>);

        const input = screen.getByRole("textbox")

        fireEvent.change(input, {target: {value: "120"}})

        expect(input).toHaveValue("100");
    });

    it('should not update input when if value is greater than maxPayment', () => {
        render(<CashSaleToPay {...mockProps}/>);

        const input = screen.getByRole("textbox")

        fireEvent.change(input, {target: {value: "170"}})

        expect(input).toHaveValue("100");
    });

    it('should not update input when if value is less than minPayment', () => {
        render(<CashSaleToPay {...mockProps}/>);

        const input = screen.getByRole("textbox")

        fireEvent.change(input, {target: {value: "-170"}})

        expect(input).toHaveValue("100");
    });
});

