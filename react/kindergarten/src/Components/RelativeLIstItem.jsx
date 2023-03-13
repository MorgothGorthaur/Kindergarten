import RelativesService from "../API/RelativesService";
import {Button, Modal} from "react-bootstrap";
import {useState} from "react";
import RelativeForm from "./RelativeForm";

function RelativeListItem({relative, tokens, setTokens, relatives, setRelatives, kidId}) {
    const [showForm, setShowForm] = useState(false);
    function handleDelete() {
        RelativesService.delete(tokens, setTokens, relative.id, kidId).then(() => {
            setRelatives(relatives.filter((r) => r.id !== relative.id));
        });
    }

    return (
        <li className="kids-list-item">
            <span>{relative.name}</span>
            <span>{relative.phone}</span>
            <span>{relative.address}</span>
            <div>
                <Button variant="primary" onClick={() => setShowForm(true)}>update</Button>
                <Button variant="danger" onClick={handleDelete}>Delete</Button>
                <Modal show={showForm} onHide={setShowForm} >
                    <RelativeForm tokens={tokens} setTokens={setTokens} relative={relative} relatives={relatives} setRelatives={setRelatives} kidId={kidId} setShowForm={setShowForm}/>
                </Modal>
            </div>
        </li>
    );
}

export default RelativeListItem;