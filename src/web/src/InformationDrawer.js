import React, { Component } from 'react';
import { ListGroup, ListGroupItem, ListGroupItemHeading, ListGroupItemText, Badge } from 'reactstrap';
class InformationDrawer extends Component
{
    render()
    {
        let str = [];
        if (this.props.dirArr)
        {
            for (let i = 0; i < this.props.dirArr.length; i++)
            {
                str.push(
                    <ListGroupItem style={{ borderRadius: 0 }} className="justify-content-between" href="#" action onClick={() => { window.dispatchEvent(new CustomEvent("_event_onDirSelect", { detail: { node: this.props.dirArr[i].CNode, angle: this.props.dirArr[i].angle } })); }}>
                        <div style={{ padding: 0, margin: 0 }}>
                            <ListGroupItemHeading style={{ padding: 0 }}>{this.props.dirArr[i].Direction}</ListGroupItemHeading>
                            <ListGroupItemText style={{ padding: 0 }}>
                                {this.props.dirArr[i].Instruction}
                            </ListGroupItemText>
                        </div>    
                        <Badge pill>{i + 1}</Badge>
                    </ListGroupItem>
                );
            }
        }
        return (
            <div style={{
                backgroundColor: "white",
                marginTop: 5,
                marginBottom: 0,
                boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)",
                maxHeight: 285,
                overflowX: "hidden",
                userSelect: "none",
                cursor: "default"
            }}>
                <ListGroup>    
                    {str}
                </ListGroup>    
            </div>
        );
    }
}
export default InformationDrawer;
