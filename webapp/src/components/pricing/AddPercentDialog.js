import React, {Component} from "react";
import {Dialog} from "primereact/dialog";
import {Button} from "primereact/button";
import PropTypes from 'prop-types';
import {ProductsService} from "../../service/ProductsService";
import {RadioButton} from "primereact/radiobutton";
import {InputNumber} from "primereact/inputnumber";
import {Dropdown} from "primereact/dropdown";

const ACTION_TYPE = {
    ADD: 'add',
    REMOVE: 'remove'
}

export class AddPercentDialog extends Component {

    static propTypes = {
        acceptCallback: PropTypes.func.isRequired
    }

    constructor(props, context) {
        super(props, context);

        this.productsService = new ProductsService();

        this.state = {
            percent:
                {
                    action: ACTION_TYPE.ADD,
                    percentType: null,
                    value: 0
                },
            percentTypes: []
        }

    }

    componentDidMount() {
        const {percentTypes} = this.state;

        if (percentTypes.length === 0) {
            this.productsService.getPercentTypes((data) => this.setState({percentTypes: data}));
        }
    }

    render() {
        return (
            <Dialog {...this.getDialogProps()} >
                {this.renderContent()}
            </Dialog>
        )
    }

    renderContent = () => {
        const {percent} = this.state;

        return (
            <div>
                <div className="p-grid p-fluid">
                    <div className="p-col-12">
                        <RadioButton id="addAction" value={ACTION_TYPE.ADD}
                                     onChange={(e) => this.handlePropertyChange('action', e.value)}
                                     checked={percent.action === ACTION_TYPE.ADD}/>
                        <label htmlFor="addAction" className="p-radiobutton-label">Agregar</label>
                    </div>

                    <div className="p-col-12">
                        <RadioButton id="removeAction" value={ACTION_TYPE.REMOVE}
                                     onChange={(e) => this.handlePropertyChange('action', e.value)}
                                     checked={percent.action === ACTION_TYPE.REMOVE}/>
                        <label htmlFor="removeAction" className="p-radiobutton-label">Remover</label>
                    </div>

                    <div className="p-col-12">
                        <label htmlFor="percentType">Tipo:</label>
                        <Dropdown id="percentType"
                                  dataKey="id"
                                  options={this.state.percentTypes}
                                  value={percent.percentType}
                                  optionLabel="nombreTipo"
                                  onChange={(e) => this.handlePropertyChange('percentType', e.value)}
                        />

                    </div>

                    <div className="p-col-12">
                        <label htmlFor="value">Valor:</label>
                        <InputNumber id="value"
                                     placeholder="Valor"
                                     maxFractionDigits={4}
                                     minFractionDigits={2}
                                     value={percent.value}
                                     showButtons
                                     mode="decimal"
                                     onChange={(e) => {
                                         this.handlePropertyChange('value', e.target.value)
                                     }}
                        />
                    </div>

                    {this.renderFooter()}
                </div>
            </div>
        );
    }

    renderFooter = () => {
        const {percent} = this.state;

        return (
            <div className="p-col-6">
                <Button label="Aceptar" icon="fa fa-fw fa-check"
                        className="p-button-success"
                        disabled={!percent.percentType}
                        onClick={this.handleAcceptButton}/>
            </div>
        );
    }

    getDialogProps = () => {
        let props = this.props;
        let defaultProps = {
            header: "Agregar porcentaje",
            modal: true,
            style: {width: "50%"}
        }

        return {...defaultProps, ...props}
    }


    handleAcceptButton = () => {
        this.props.onHide();

        if (this.props.acceptCallback) {
            this.props.acceptCallback(this.state.percent);
        }
    }

    handlePropertyChange = (property, value) => {
        let {percent} = this.state;

        percent[property] = value;

        this.setState({percent: percent});
    }

}
