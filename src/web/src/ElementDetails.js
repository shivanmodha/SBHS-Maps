import React, { Component } from 'react';
import { Button, ListGroupItem } from 'reactstrap';
import { Icon } from 'react-fa';
class ElementDetails extends Component
{
    render()
    {
        let name = "";
        if (this.props.element)
        {
            name = this.props.element.Name;
        }    
        return (
            <ListGroupItem style={{
                backgroundColor: "rgba(255, 255, 255, 0.5)",
                position: "fixed",
                padding: 0,
                width: 300,
                top: 10,
                right: 10,
                boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)"
            }} className="justify-content-between">
                <div style={{ maxWidth: 250 }}>
                    <div>
                        <p className="lead" style={{ margin: 10 }}>{name}</p>
                    </div>
                    <Button>Test</Button>
                </div>
                <Button outline style={{ position: "fixed", right: 8, top: 10, borderRadius: 0, border: 0, marginRight: 2 }} color="danger"><Icon style={{ padding: 0, margin: 0 }} name="close" /></Button>
            </ListGroupItem>
        );
    }
}
export default ElementDetails;