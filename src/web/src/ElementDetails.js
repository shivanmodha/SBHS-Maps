import React, { Component } from 'react';
import { Button, ListGroupItem } from 'reactstrap';
import { Icon } from 'react-fa';
class ElementDetails extends Component
{
    _render_element(i)
    {
        let name = "";
        let type = "";
        if (this.props.elements[i])
        {
            name = this.props.elements[i].Name;
            type = this.props.elements[i].Type;
        }
        if (type === "Room")
        {
            type = "Classroom";
        }
        return (
            <ListGroupItem style={{ borderRadius: 0, margin: 0, marginTop: 10, padding: 0, width: "100%", boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)" }}>
                <div>
                    <ListGroupItem style={{ borderRadius: 0, margin: 0, padding: 0, width: 298, border: "none" }} className="justify-content-between">
                        <div style={{ maxWidth: 250 }}>
                            <p className="lead" style={{ margin: 10, fontSize: 15 }}>{name}</p>
                        </div>
                        <Button outline style={{ borderRadius: 0, border: 0, marginTop: -10, marginRight: -1 }} color="secondary" onClick={() =>
                        {
                            window.dispatchEvent(new CustomEvent("_event_onElementDetailRemove", { detail: { index: i } }));
                        }}><Icon style={{ padding: 0, margin: 0 }} name="close" /></Button>
                    </ListGroupItem>
                    <p style={{ margin: 10, marginTop: 0, fontSize: 13 }}><i>{type}</i></p>
                    <Button outline color="secondary" style={{ borderRadius: 0, borderBottom: 0, borderLeft: 0, borderRight: 0, marginBottom: -1, marginLeft: -1 }} onClick={() =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetDirection", { detail: { dir: "from", element: this.props.elements[i] } }));  
                    }}><small>Directions From</small></Button>
                    <Button outline color="secondary" style={{ borderRadius: 0, borderBottom: 0, borderLeft: 0, borderRight: 0, marginBottom: -1 }} onClick={() =>
                    {
                        window.dispatchEvent(new CustomEvent("_event_onSetDirection", { detail: { dir: "to", element: this.props.elements[i] } }));  
                    }}><small>Directions To</small></Button>
                </div>
            </ListGroupItem>
        );
    }
    _render_elements()
    {
        if (this.props.elements)
        {
            let itm = [];
            for (let i = 0; i < this.props.elements.length; i++)
            {
                itm.push(this._render_element(i));
            }
            return (
                <div>
                    {itm}
                </div>
            );
        }
        else
        {
            return null;
        }
    }
    render()
    {
        return (
            <div style={{
                backgroundColor: "rgba(255, 255, 255, 0)",
                position: "fixed",
                padding: 0,
                width: 300,
                top: 0,
                right: 10,
                userSelect: "none",
                cursor: "default"
            }}>
                {this._render_elements()}    
            </div>
        );
    }
}
export default ElementDetails;