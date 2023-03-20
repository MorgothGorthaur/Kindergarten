import {useEffect, useState} from "react";
import GroupService from "../API/GroupService";
import {Button, Modal} from "react-bootstrap";
import GroupForm from "./GroupForm";
import KidsMenu from "./KidsMenu";
import Loader from "../UI/Loader/Loader";
import React from "react";

function Group({tokens, setTokens}) {
    const [group, setGroup] = useState()
    const [showAddForm, setShowAddForm] = useState(false);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [showKids, setShowKids] = useState(false);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        if (loader) {
            GroupService.getGroup(tokens, setTokens).then(data => {
                    console.log(data);
                    setGroup(data);
                    setLoader(false);
                }
            )
        }
    }, [loader]);
    const handleDelete = () => {
        GroupService.delete(tokens, setTokens).then(data => {
            console.log(data);
            if (!data) {
                setGroup()
            } else alert(data.debugMessage);
        });
    }
    return (
        <div>
            {group ? (
                <div>
                    <div className="elem-info">
                        <h3>Group name : {group.name}</h3>
                        <h3>Max. size: {group.maxSize}</h3>
                        <h3>Curr. size: {group.currentSize}</h3>
                    </div>
                    <div className="elem-actions">
                        <Button variant="primary" onClick={() => setShowUpdateForm(true)}>update</Button>
                        <Button variant="danger" onClick={() => handleDelete()}>delete</Button>
                        <Modal show={showUpdateForm} onHide={setShowUpdateForm}>
                            <GroupForm group={group} setGroup={setGroup} tokens={tokens}
                                       setTokens={setTokens}
                                       setShowForm={setShowUpdateForm}/>
                        </Modal>
                    </div>
                    <div>
                        {
                            showKids ?
                                <div>
                                    <Button variant="dark" onClick={() => setShowKids(false)}> close </Button>
                                    <KidsMenu tokens={tokens} setTokens={setTokens} group={group} setGroup={setGroup}/>
                                </div>
                                :
                                <Button variant="secondary" onClick={() => setShowKids(true)}> getKidsMenu </Button>
                        }
                    </div>
                </div>

            ) : loader ? (
                <div style={{
                    textAlign: 'center',
                    height: '100vh',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}>
                    <Loader/>
                </div>
            ) : (
                <div>
                    <Button onClick={() => setShowAddForm(true)}>Add group</Button>
                    <Modal show={showAddForm} onHide={setShowAddForm}> <GroupForm setGroup={setGroup} tokens={tokens}
                                                                                  setTokens={setTokens}
                                                                                  setShowForm={setShowAddForm}/>
                    </Modal>
                </div>
            )}
        </div>
    );
}

export default Group;
