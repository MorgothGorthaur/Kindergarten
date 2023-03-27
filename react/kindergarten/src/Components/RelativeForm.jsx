import {useEffect, useState} from "react";
import {Button, Form} from "react-bootstrap";
import Input from "../UI/Input/Input";
import RelativesService from "../API/RelativesService";

const RelativeForm = ({tokens, setTokens, relatives, setRelatives, relative, setShowForm, kidId}) => {
    const [id, setId] = useState();
    const [name, setName] = useState();
    const [phone, setPhone] = useState();
    const [address, setAddress] = useState();
    useEffect(() => {
        if (relative) {
            setId(relative.id);
            setName(relative.name);
            setPhone(relative.phone);
            setAddress(relative.address);
        }
    }, [relative]);
    const add = (e) => {
        e.preventDefault();
        RelativesService.add(tokens, setTokens, kidId, name, phone, address).then(data => {
            console.log(data);
            if (data.debugMessage) alert(data.debugMessage)
            else {
                setRelatives([...relatives.filter(k => k.id !== data.id), data]);
                setShowForm(false);
            }
        })
    }
    const update = (e) => {
        e.preventDefault();
        RelativesService.update(tokens, setTokens, kidId, id, name, phone, address).then(data => {
            console.log(data);
            if (data.debugMessage) {
                alert(data.debugMessage);

            } else {
                setRelatives([...relatives.filter(k => k.id !== id), {
                    id: data.id,
                    name: name,
                    phone: phone,
                    address: address
                }])
                setShowForm(false);
            }
        })
    }
    return (
        <Form className="form" onSubmit={relative ? update : add}>
            <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
            <Input type="text" placeholder="phone" value={phone} onChange={e => setPhone(e.target.value)}/>
            <Input type="text" placeholder="address" value={address} onChange={e => setAddress(e.target.value)}/>
            <Button type="submit"> {relative ? "update" : "add"} </Button>
        </Form>
    );
};
export default RelativeForm;