import {Button, Modal} from "react-bootstrap";
import KidsService from "../API/KidsService";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import ChildForm from "./ChildForm";
import {useState} from "react";
import Relatives from "./Relatives";

function KidListItem({kid, tokens, setTokens, kids, setKids, group, setGroup}) {
    const [updateChildForm, setUpdateChildForm] = useState(false);
    const [brothersAndSisters, setBrothersAndSisters] = useState(false);
    const [relatives, setRelatives] = useState(false);
    const handleDelete = () => {
        KidsService.delete(kid.id, tokens, setTokens).then((data) => {
            console.log(data);
            if (!data) {
                setKids([...kids.filter((k) => k.id !== kid.id)]);
                setGroup({ ...group, currentSize: group.currentSize - 1 });
            } else alert(data.debugMessage);
        });
    };
    return (
        <div>
            <div>
                <li className="kids-list-item" key={kid.id}>
                    <span className="kid-name">{kid.name}</span>
                    <span className="birth-year">({kid.birthYear})</span>
                    <div className="button-container">
                        <Button variant="primary" onClick={() => setUpdateChildForm(true)}>update</Button>
                        <Button variant="danger" onClick={() => handleDelete()}>delete</Button>
                        <Button variant="primary" onClick={() => setBrothersAndSisters(true)}>brothers and sisters</Button>
                        {
                            !relatives ? <Button variant="secondary" onClick={() => setRelatives(true)}> relatives </Button>
                                : <Button variant="dark" onClick={() => setRelatives(false)}> close</Button>
                        }
                    </div>
                </li>
                <Modal show={brothersAndSisters} onHide={setBrothersAndSisters}>
                    <KidBrothersAndSisters id={kid.id} tokens={tokens} setTokens={setTokens}/>
                </Modal>
                <Modal show={updateChildForm} onHide={setUpdateChildForm}>
                    <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                               setShowForm={setUpdateChildForm} child={kid}/>
                </Modal>
            </div>
            <div>
                {
                    relatives ? <Relatives tokens={tokens} setTokens={setTokens} kidId={kid.id} /> : <br/>
                }
            </div>
        </div>
    );
}

export default KidListItem;