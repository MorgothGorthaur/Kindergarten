import {useEffect, useState} from "react";
import GroupService from "../API/GroupService";
import {Button, Modal} from "react-bootstrap";
import GroupForm from "./GroupForm";
import KidsMenu from "./KidsMenu";

function Group({tokens, setTokens}) {
    const [group, setGroup] = useState()
    const [showAddForm, setShowAddForm] = useState(false);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [showKids, setShowKids] = useState(false);
    useEffect(() => {
        if (!group) {
            GroupService.getGroup(tokens, setTokens).then(data => {
                    console.log(data);
                    if (data.debugMessage) alert(data.debugMessage)
                    else setGroup(data);


                }
            )
        }
    }, [group]);
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
                                    <KidsMenu tokens={tokens} setTokens={setTokens} group={group} setGroup={setGroup} />
                                </div>
                                :
                                <Button variant="secondary" onClick={() => setShowKids(true)}> getKidsMenu </Button>
                        }
                    </div>
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
